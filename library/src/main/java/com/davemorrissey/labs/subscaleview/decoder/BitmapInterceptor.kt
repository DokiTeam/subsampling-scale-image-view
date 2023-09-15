package com.davemorrissey.labs.subscaleview.decoder

import android.graphics.Bitmap
import androidx.annotation.WorkerThread

public fun interface BitmapInterceptor {

	@WorkerThread
	public fun intercept(bitmap: Bitmap, isTile: Boolean): Bitmap
}
