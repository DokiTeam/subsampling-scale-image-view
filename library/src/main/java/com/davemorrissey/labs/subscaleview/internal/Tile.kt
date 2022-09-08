package com.davemorrissey.labs.subscaleview.internal

import android.graphics.Bitmap
import android.graphics.Rect

internal class Tile {
	var sRect: Rect? = null
	var sampleSize = 0
	var bitmap: Bitmap? = null
	var isLoading = false
	var isVisible = false

	// Volatile fields instantiated once then updated before use to reduce GC.
	val vRect = Rect()
	val fileSRect = Rect()

	fun recycle() {
		bitmap?.recycle()
		bitmap = null
	}
}
