package com.davemorrissey.labs.subscaleview.decoder

import android.app.ActivityManager
import android.content.Context
import android.graphics.BitmapRegionDecoder
import android.os.Build
import java.io.InputStream

internal fun BitmapRegionDecoder(pathName: String): BitmapRegionDecoder {
	return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
		BitmapRegionDecoder.newInstance(pathName)
	} else {
		@Suppress("DEPRECATION")
		BitmapRegionDecoder.newInstance(pathName, false)
	}
}

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
