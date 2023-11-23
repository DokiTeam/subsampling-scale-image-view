package com.davemorrissey.labs.subscaleview.decoder

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.graphics.*
import android.net.Uri
import android.text.TextUtils
import androidx.annotation.WorkerThread
import com.davemorrissey.labs.subscaleview.internal.ASSET_PREFIX
import com.davemorrissey.labs.subscaleview.internal.FILE_PREFIX
import com.davemorrissey.labs.subscaleview.internal.RESOURCE_PREFIX
import com.davemorrissey.labs.subscaleview.internal.ZIP_PREFIX
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.zip.ZipFile

/**
 * Default implementation of [com.davemorrissey.labs.subscaleview.decoder.ImageRegionDecoder]
 * using Android's [android.graphics.BitmapRegionDecoder], based on the Skia library. This
 * works well in most circumstances and has reasonable performance due to the cached decoder instance,
 * however it has some problems with grayscale, indexed and CMYK images.
 *
 * A [ReadWriteLock] is used to delegate responsibility for multi threading behaviour to the
 * [BitmapRegionDecoder] instance on SDK &gt;= 21, whilst allowing this class to block until no
 * tiles are being loaded before recycling the decoder. In practice, [BitmapRegionDecoder] is
 * synchronized internally so this has no real impact on performance.
 */
public class SkiaImageRegionDecoder @JvmOverloads constructor(
	private val bitmapConfig: Bitmap.Config = Bitmap.Config.RGB_565,
) : ImageRegionDecoder {

	private var decoder: BitmapRegionDecoder? = null
	private val decoderLock: ReadWriteLock = ReentrantReadWriteLock(true)

	@SuppressLint("DiscouragedApi")
	@Throws(Exception::class)
	@WorkerThread
	override fun init(context: Context, uri: Uri): Point {
		val uriString = uri.toString()
		decoder = when {
			uriString.startsWith(RESOURCE_PREFIX) -> {
				val packageName = uri.authority
				val res = if (packageName == null || context.packageName == packageName) {
					context.resources
				} else {
					context.packageManager.getResourcesForApplication(packageName)
				}
				var id = 0
				val segments = uri.pathSegments
				val size = segments.size
				if (size == 2 && segments[0] == "drawable") {
					val resName = segments[1]
					id = res.getIdentifier(resName, "drawable", packageName)
				} else if (size == 1 && TextUtils.isDigitsOnly(segments[0])) {
					try {
						id = segments[0].toInt()
					} catch (ignored: NumberFormatException) {
					}
				}
				context.resources.openRawResource(id).use { BitmapRegionDecoder(it) }
			}

			uriString.startsWith(ASSET_PREFIX) -> {
				val assetName = uriString.substring(ASSET_PREFIX.length)
				context.assets.open(assetName, AssetManager.ACCESS_RANDOM).use { BitmapRegionDecoder(it) }
			}

			uriString.startsWith(ZIP_PREFIX) -> {
				val file = ZipFile(uriString.substring(FILE_PREFIX.length).substringBeforeLast('#'))
				val entry = file.getEntry(uriString.substringAfterLast('#'))
				file.use { BitmapRegionDecoder(it.getInputStream(entry)) }
			}

			uriString.startsWith(FILE_PREFIX) -> {
				BitmapRegionDecoder(uriString.substring(FILE_PREFIX.length))
			}

			else -> {
				val contentResolver = context.contentResolver
				contentResolver.openInputStream(uri)?.use { BitmapRegionDecoder(it) }
					?: error("Content resolver returned null stream. Unable to initialise with uri.")
			}
		}
		return Point(decoder!!.width, decoder!!.height)
	}

	@WorkerThread
	override fun decodeRegion(sRect: Rect, sampleSize: Int): Bitmap {
		decodeLock.lock()
		return try {
			check(decoder?.isRecycled == false) {
				"Cannot decode region after decoder has been recycled"
			}
			val options = BitmapFactory.Options()
			options.inSampleSize = sampleSize
			options.inPreferredConfig = bitmapConfig
			decoder?.decodeRegion(sRect, options)
				?: error("Skia image decoder returned null bitmap - image format may not be supported")
		} finally {
			decodeLock.unlock()
		}
	}

	@get:Synchronized
	override val isReady: Boolean
		get() = decoder?.isRecycled == false

	@Synchronized
	override fun recycle() {
		decoderLock.writeLock().lock()
		try {
			decoder?.recycle()
			decoder = null
		} finally {
			decoderLock.writeLock().unlock()
		}
	}

	/**
	 * Before SDK 21, BitmapRegionDecoder was not synchronized internally. Any attempt to decode
	 * regions from multiple threads with one decoder instance causes a segfault. For old versions
	 * use the write lock to enforce single threaded decoding.
	 */
	private val decodeLock: Lock
		get() = decoderLock.readLock()

	public class Factory @JvmOverloads constructor(
		override val bitmapConfig: Bitmap.Config = Bitmap.Config.RGB_565,
	) : DecoderFactory<SkiaImageRegionDecoder> {

		override fun make(): SkiaImageRegionDecoder {
			return SkiaImageRegionDecoder(bitmapConfig)
		}
	}
}
