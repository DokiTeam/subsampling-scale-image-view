package com.davemorrissey.labs.subscaleview.internal

import android.graphics.PointF
import com.davemorrissey.labs.subscaleview.OnAnimationEventListener
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.Companion.EASE_IN_OUT_QUAD
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.Companion.ORIGIN_ANIM

internal class Anim(
	/** Scale at start of anim **/
	val scaleStart: Float,
	/** Scale at end of anim (target) **/
	val scaleEnd: Float,
	/** Source center point at start **/
	val sCenterStart: PointF,
	/** Source center point at end, adjusted for pan limits **/
	val sCenterEnd: PointF,
	/** Source center point that was requested, without adjustment **/
	val sCenterEndRequested: PointF,
	/** View point that was double tapped **/
	val vFocusStart: PointF?,
	/** Where the view focal point should be moved to during the anim **/
	val vFocusEnd: PointF,
	/** How long the anim takes **/
	val duration: Long = 500,
	/** Whether the anim can be interrupted by a touch **/
	val interruptible: Boolean = true,
	/** Easing style **/
	val easing: Int = EASE_IN_OUT_QUAD,
	/** Animation origin (API, double tap or fling) **/
	val origin: Int = ORIGIN_ANIM,
	/** Start time **/
	val time: Long,
	/** Event listener **/
	val listener: OnAnimationEventListener?,
)
