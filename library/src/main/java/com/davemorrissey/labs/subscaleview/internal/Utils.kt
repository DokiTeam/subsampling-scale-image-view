package com.davemorrissey.labs.subscaleview.internal

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.exifinterface.media.ExifInterface
import com.davemorrissey.labs.subscaleview.SCHEME_ASSET
import com.davemorrissey.labs.subscaleview.SCHEME_FILE
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.Companion.VALID_ORIENTATIONS
import kotlin.math.hypot

/**
 * Helper method for setting the values of a tile matrix array.
 */
internal fun setMatrixArray(
	array: FloatArray,
	f0: Float,
	f1: Float,
	f2: Float,
	f3: Float,
	f4: Float,
	f5: Float,
	f6: Float,
	f7: Float,
) {
	array[0] = f0
	array[1] = f1
	array[2] = f2
	array[3] = f3
	array[4] = f4
	array[5] = f5
	array[6] = f6
	array[7] = f7
}

/**
 * Pythagoras distance between two points.
 */
internal fun distance(x0: Float, x1: Float, y0: Float, y1: Float): Float {
	return hypot(x0 - x1, y0 - y1)
}

@WorkerThread
internal fun getExifOrientation(context: Context, sourceUri: String): Int {
	var exifOrientation = SubsamplingScaleImageView.ORIENTATION_0
	if (sourceUri.startsWith(ContentResolver.SCHEME_CONTENT)) {
		var cursor: Cursor? = null
		try {
			val columns = arrayOf(MediaStore.Images.Media.ORIENTATION)
			cursor = context.contentResolver.query(Uri.parse(sourceUri), columns, null, null, null)
			if (cursor != null && cursor.moveToFirst()) {
				val orientation = cursor.getInt(0)
				if (orientation in VALID_ORIENTATIONS && orientation != SubsamplingScaleImageView.ORIENTATION_USE_EXIF) {
					exifOrientation = orientation
				} else {
					Log.w(SubsamplingScaleImageView.TAG, "Unsupported orientation: $orientation")
				}
			}
		} catch (e: Exception) {
			Log.w(SubsamplingScaleImageView.TAG, "Could not get orientation of image from media store")
		} finally {
			cursor?.close()
		}
	} else if (sourceUri.startsWith(SCHEME_FILE) && !sourceUri.startsWith(SCHEME_ASSET)) {
		try {
			val exifInterface = ExifInterface(sourceUri.substring(SCHEME_FILE.length - 1))
			when (
				val orientationAttr =
					exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
			) {
				ExifInterface.ORIENTATION_NORMAL, ExifInterface.ORIENTATION_UNDEFINED ->
					exifOrientation =
						SubsamplingScaleImageView.ORIENTATION_0
				ExifInterface.ORIENTATION_ROTATE_90 -> exifOrientation = SubsamplingScaleImageView.ORIENTATION_90
				ExifInterface.ORIENTATION_ROTATE_180 -> exifOrientation = SubsamplingScaleImageView.ORIENTATION_180
				ExifInterface.ORIENTATION_ROTATE_270 -> exifOrientation = SubsamplingScaleImageView.ORIENTATION_270
				else -> Log.w(SubsamplingScaleImageView.TAG, "Unsupported EXIF orientation: $orientationAttr")
			}
		} catch (e: Exception) {
			Log.w(SubsamplingScaleImageView.TAG, "Could not get EXIF orientation of image")
		}
	}
	return exifOrientation
}
