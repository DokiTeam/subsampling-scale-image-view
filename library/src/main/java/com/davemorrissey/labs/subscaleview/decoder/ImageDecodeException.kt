package com.davemorrissey.labs.subscaleview.decoder

import android.net.Uri
import java.io.IOException

public class ImageDecodeException(
	public val uri: Uri?,
) : IOException(MESSAGE) {

	private companion object {

		const val MESSAGE = "Image decoder returned null bitmap - image format may not be supported"
	}
}
