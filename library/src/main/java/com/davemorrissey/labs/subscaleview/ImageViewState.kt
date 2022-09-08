package com.davemorrissey.labs.subscaleview

import android.graphics.PointF
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
public class ImageViewState(
	public val scale: Float,
	public val center: PointF, // TODO 2 fields
	public val orientation: Int,
) : Parcelable
