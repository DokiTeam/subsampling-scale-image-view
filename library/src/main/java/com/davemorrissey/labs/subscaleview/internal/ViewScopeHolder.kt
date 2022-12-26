package com.davemorrissey.labs.subscaleview.internal

import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@DelicateCoroutinesApi
internal class ViewScopeHolder(
	private val context: CoroutineContext,
) : View.OnAttachStateChangeListener, ReadOnlyProperty<View, CoroutineScope> {

	private var cachedScope: CoroutineScope? = null

	override fun getValue(thisRef: View, property: KProperty<*>): CoroutineScope {
		cachedScope?.let { return it }
		return createNewScope(thisRef).also {
			cachedScope = it
		}
	}

	override fun onViewAttachedToWindow(v: View) = Unit

	override fun onViewDetachedFromWindow(v: View) {
		v.removeOnAttachStateChangeListener(this)
		cachedScope?.cancel()
		cachedScope = null
	}

	private fun createNewScope(view: View): CoroutineScope {
		if (SubsamplingScaleImageView.isDebug && !ViewCompat.isAttachedToWindow(view)) {
			Log.w(
				SubsamplingScaleImageView.TAG,
				"Coroutine scope requested, but view is not attached to window yet",
			)
		}
		val scope = CoroutineScope(context + SupervisorJob(context[Job]))
		cachedScope = scope
		view.addOnAttachStateChangeListener(this)
		return scope
	}
}
