package com.davemorrissey.labs.subscaleview

import android.graphics.Bitmap as AndroidBitmap
import android.graphics.Rect
import android.net.Uri as AndroidUri
import androidx.annotation.DrawableRes
import androidx.annotation.ReturnThis
import com.davemorrissey.labs.subscaleview.internal.SCHEME_ASSET
import com.davemorrissey.labs.subscaleview.internal.SCHEME_FILE

public sealed class ImageSource(
	public var isTilingEnabled: Boolean,
) {

	internal var region: Rect? = null
		set(value) {
			field = value
			if (value != null) {
				sWidth = value.width()
				sHeight = value.height()
			}
		}

	public var sWidth: Int = 0
		protected set
	public var sHeight: Int = 0
		protected set

	public class Resource(@DrawableRes public val resourceId: Int) : ImageSource(true)

	public class Uri(public val uri: AndroidUri) : ImageSource(true)

	public class Bitmap @JvmOverloads constructor(
		public val bitmap: AndroidBitmap,
		public val isCached: Boolean = false,
	) : ImageSource(false) {
		init {
			sWidth = bitmap.width
			sHeight = bitmap.height
		}
	}

	@ReturnThis
	public fun region(left: Int, top: Int, right: Int, bottom: Int): ImageSource {
		region = Rect(left, top, right, bottom)
		return this
	}

	@ReturnThis
	public fun region(rect: Rect): ImageSource {
		region = Rect(rect)
		return this
	}

	public companion object {

		@JvmStatic
		public fun Uri(uri: String): Uri {
			var uriString = uri
			if (!uriString.contains("://")) {
				if (uriString.startsWith("/")) {
					uriString = uriString.substring(1)
				}
				uriString = SCHEME_FILE + uriString
			}
			return Uri(AndroidUri.parse(uriString))
		}

		@JvmStatic
		@Suppress("FunctionName")
		public fun Asset(assetName: String): Uri {
			return Uri(AndroidUri.parse(SCHEME_ASSET + assetName))
		}
	}
}
