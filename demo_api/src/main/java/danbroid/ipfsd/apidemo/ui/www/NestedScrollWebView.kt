/*
 * Copyright (C) 2016 Tobias Rohloff
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package danbroid.ipfsd.apidemo.ui.www

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebView
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat

class NestedScrollWebView : WebView, NestedScrollingChild {

  private var lastMotionY = 0
  private val scrollOffset = IntArray(2)
  private val scrollConsumed = IntArray(2)
  private var nestedYOffset = 0

  val childHelper = NestedScrollingChildHelper(this)

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
    context,
    attrs,
    defStyleAttr
  )


  init {
    isNestedScrollingEnabled = true
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    var result = false
    val trackedEvent = MotionEvent.obtain(event)
    val action = event.action
    if (action == MotionEvent.ACTION_DOWN) {
      nestedYOffset = 0
    }
    val y = event.y.toInt()
    event.offsetLocation(0f, nestedYOffset.toFloat())
    when (action) {
      MotionEvent.ACTION_DOWN -> {
        lastMotionY = y
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
        result = super.onTouchEvent(event)
      }
      MotionEvent.ACTION_MOVE -> {
        var deltaY = lastMotionY - y
        if (dispatchNestedPreScroll(0, deltaY, scrollConsumed, scrollOffset)) {
          deltaY -= scrollConsumed[1]
          trackedEvent.offsetLocation(0f, scrollOffset[1].toFloat())
          nestedYOffset += scrollOffset[1]
        }
        lastMotionY = y - scrollOffset[1]
        val oldY = scrollY
        val newScrollY = Math.max(0, oldY + deltaY)
        val dyConsumed = newScrollY - oldY
        val dyUnconsumed = deltaY - dyConsumed
        if (dispatchNestedScroll(0, dyConsumed, 0, dyUnconsumed, scrollOffset)) {
          lastMotionY -= scrollOffset[1]
          trackedEvent.offsetLocation(0f, scrollOffset[1].toFloat())
          nestedYOffset += scrollOffset[1]
        }
        result = super.onTouchEvent(trackedEvent)
        trackedEvent.recycle()
      }
      MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
        stopNestedScroll()
        result = super.onTouchEvent(event)
      }
    }
    return result
  }

  override fun setNestedScrollingEnabled(enabled: Boolean) {
    childHelper.isNestedScrollingEnabled = enabled
  }

  override fun isNestedScrollingEnabled(): Boolean {
    return childHelper.isNestedScrollingEnabled
  }

  override fun startNestedScroll(axes: Int): Boolean {
    return childHelper.startNestedScroll(axes)
  }

  override fun stopNestedScroll() {
    childHelper.stopNestedScroll()
  }

  override fun hasNestedScrollingParent(): Boolean {
    return childHelper.hasNestedScrollingParent()
  }

  override fun dispatchNestedScroll(
    dxConsumed: Int,
    dyConsumed: Int,
    dxUnconsumed: Int,
    dyUnconsumed: Int,
    offsetInWindow: IntArray?
  ) = childHelper.dispatchNestedScroll(
    dxConsumed,
    dyConsumed,
    dxUnconsumed,
    dyUnconsumed,
    offsetInWindow
  )


  override fun dispatchNestedPreScroll(
    dx: Int,
    dy: Int,
    consumed: IntArray?,
    offsetInWindow: IntArray?
  ): Boolean {
    return childHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
  }

  override fun dispatchNestedFling(
    velocityX: Float,
    velocityY: Float,
    consumed: Boolean
  ): Boolean {
    return childHelper.dispatchNestedFling(velocityX, velocityY, consumed)
  }

  override fun dispatchNestedPreFling(
    velocityX: Float,
    velocityY: Float
  ): Boolean {
    return childHelper.dispatchNestedPreFling(velocityX, velocityY)
  }
}