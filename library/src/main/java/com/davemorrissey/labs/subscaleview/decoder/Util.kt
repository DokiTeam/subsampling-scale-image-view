package com.davemorrissey.labs.subscaleview.decoder

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.ContentResolver
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import androidx.annotation.WorkerThread
import com.davemorrissey.labs.subscaleview.ImageSource
import org.jetbrains.annotations.Blocking
import java.io.InputStream

@Blocking
internal fun BitmapRegionDecoder(pathName: String): BitmapRegionDecoder {
	return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
		BitmapRegionDecoder.newInstance(pathName)
	} else {
		@Suppress("DEPRECATION")
		BitmapRegionDecoder.newInstance(pathName, false)
	}
}

@Blocking
internal fun BitmapRegionDecoder(inputStream: InputStream): BitmapRegionDecoder {
	return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
		BitmapRegionDecoder.newInstance(inputStream)
	} else {
		@Suppress("DEPRECATION")
		BitmapRegionDecoder.newInstance(inputStream, false)
	} ?: throw RuntimeException("Cannot instantiate BitmapRegionDecoder")
}

internal fun Context.isLowMemory(): Boolean {
	val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
	return if (activityManager != null) {
		val memoryInfo = ActivityManager.MemoryInfo()
		activityManager.getMemoryInfo(memoryInfo)
		memoryInfo.lowMemory
	} else {
		true
	}
}

@WorkerThread
@Blocking
@SuppressLint("DiscouragedApi")
internal fun decodeResource(context: Context, uri: Uri, options: BitmapFactory.Options): Bitmap {
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
	return BitmapFactory.decodeResource(res, id, options)
}

@WorkerThread
@Blocking
internal fun ContentResolver.decodeBitmap(uri: Uri, options: BitmapFactory.Options): Bitmap? {
	return openInputStream(uri)?.use { inputStream ->
		BitmapFactory.decodeStream(inputStream, null, options)
	}
}

@WorkerThread
@Blocking
internal fun AssetManager.decodeBitmap(name: String, options: BitmapFactory.Options): Bitmap? {
	return open(name).use { inputStream ->
		BitmapFactory.decodeStream(inputStream, null, options)
	}
}

internal fun ImageSource.toUri(context: Context): Uri = when (this) {
	is ImageSource.Resource -> Uri.parse(
		ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/" + resourceId,
	)

	is ImageSource.Uri -> uri
	is ImageSource.Bitmap -> throw IllegalArgumentException("Bitmap source cannot be represented as Uri")
}
