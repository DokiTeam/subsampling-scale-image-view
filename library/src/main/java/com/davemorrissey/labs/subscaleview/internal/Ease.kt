package com.davemorrissey.labs.subscaleview.internal

import android.view.animation.Interpolator
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.Companion.EASE_IN_OUT_QUAD
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.Companion.EASE_OUT_QUAD

/**
 * Apply a selected type of easing.
 * @param type Easing type, from static fields
 * @param time Elapsed time
 * @param from Start value
 * @param change Target value
 * @param duration Anm duration
 * @return Current value
 */
internal fun ease(type: Int, time: Long, from: Float, change: Float, duration: Long): Float {
	return when (type) {
		EASE_IN_OUT_QUAD -> easeInOutQuad(time, from, change, duration)
		EASE_OUT_QUAD -> easeOutQuad(time, from, change, duration)
		else -> throw IllegalStateException("Unexpected easing type: $type")
	}
}

/**
 * Quadratic easing for fling. With thanks to Robert Penner - http://gizma.com/easing/
 * @param time Elapsed time
 * @param from Start value
 * @param change Target value
 * @param duration Anm duration
 * @return Current value
 */
private fun easeOutQuad(time: Long, from: Float, change: Float, duration: Long): Float {
	val progress = time.toFloat() / duration.toFloat()
	return -change * progress * (progress - 2) + from
}

/**
 * Quadratic easing for scale and center animations. With thanks to Robert Penner - http://gizma.com/easing/
 * @param time Elapsed time
 * @param from Start value
 * @param change Target value
 * @param duration Anm duration
 * @return Current value
 */
private fun easeInOutQuad(time: Long, from: Float, change: Float, duration: Long): Float {
	var timeF = time / (duration / 2f)
	return if (timeF < 1) {
		change / 2f * timeF * timeF + from
	} else {
		timeF--
		-change / 2f * (timeF * (timeF - 2) - 1) + from
	}
}

internal fun ease(interpolator: Interpolator, time: Long, from: Float, change: Float, duration: Long): Float {
	val fraction = interpolator.getInterpolation(time.toFloat() / duration.toFloat())
	return from + change * fraction
}
