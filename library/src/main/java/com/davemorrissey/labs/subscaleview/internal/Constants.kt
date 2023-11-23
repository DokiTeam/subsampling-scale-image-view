package com.davemorrissey.labs.subscaleview.internal

import android.content.ContentResolver

internal const val FILE_PREFIX = "file://"
internal const val ZIP_PREFIX = "file+zip://"
internal const val ASSET_PREFIX = "$FILE_PREFIX/android_asset/"
internal const val RESOURCE_PREFIX = "${ContentResolver.SCHEME_ANDROID_RESOURCE}://"
internal const val DECODER_NULL_MESSAGE = "Image decoder returned null bitmap - image format may not be supported"
internal const val SCHEME_ASSET = "file:///android_asset/"
internal const val SCHEME_FILE = "file:///"
internal const val SCHEME_ZIP = "file+zip"
