package com.davemorrissey.labs.subscaleview

import android.graphics.PointF

/**
 * Default implementation of {@link OnStateChangedListener}. This does nothing in any method.
 */
public interface DefaultOnStateChangedListener : OnStateChangedListener {

	/**
	 * @inherit
	 */
	override fun onScaleChanged(newScale: Float, origin: Int): Unit = Unit

	/**
	 * @inherit
	 */
	override fun onCenterChanged(newCenter: PointF, origin: Int): Unit = Unit
}
