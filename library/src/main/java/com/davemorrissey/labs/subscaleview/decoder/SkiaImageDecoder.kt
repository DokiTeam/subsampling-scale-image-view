package com.davemorrissey.labs.subscaleview.decoder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.Companion.preferredBitmapConfig
import com.davemorrissey.labs.subscaleview.internal.ASSET_PREFIX
import com.davemorrissey.labs.subscaleview.internal.DECODER_NULL_MESSAGE
import com.davemorrissey.labs.subscaleview.internal.FILE_PREFIX
import com.davemorrissey.labs.subscaleview.internal.RESOURCE_PREFIX

/**
 * Default implementation of [com.davemorrissey.labs.subscaleview.decoder.ImageDecoder]
 * using Android's [android.graphics.BitmapFactory], based on the Skia library. This
 * works well in most circumstances and has reasonable performance, however it has some problems
 * with grayscale, indexed and CMYK images.
 */
public class SkiaImageDecoder @JvmOverloads constructor(
	bitmapConfig: Bitmap.Config? = null,
) : ImageDecoder {

	private val bitmapConfig = bitmapConfig ?: preferredBitmapConfig ?: Bitmap.Config.RGB_565

	@SuppressLint("DiscouragedApi")
	@Throws(Exception::class)
	override fun decode(context: Context, uri: Uri): Bitmap {
		val uriString = uri.toString()
		val options = BitmapFactory.Options()
		options.inPreferredConfig = bitmapConfig
		return when {
			uriString.startsWith(RESOURCE_PREFIX) -> {
				decodeResource(context, uri, options)
			}
			uriString.startsWith(ASSET_PREFIX) -> {
				val assetName = uriString.substring(ASSET_PREFIX.length)
				context.assets.decodeBitmap(assetName, options)
			}
			uriString.startsWith(FILE_PREFIX) -> {
				BitmapFactory.decodeFile(uriString.substring(FILE_PREFIX.length), options)
			}
			else -> {
				context.contentResolver.decodeBitmap(uri, options)
			}
		} ?: throw RuntimeException(DECODER_NULL_MESSAGE)
	}

	public class Factory @JvmOverloads constructor(
		private val bitmapConfig: Bitmap.Config? = null
	) : DecoderFactory<SkiaImageDecoder> {

		override fun make(): SkiaImageDecoder {
			return SkiaImageDecoder(bitmapConfig)
		}
	}
}
