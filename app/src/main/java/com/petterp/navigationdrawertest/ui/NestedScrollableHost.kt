package com.petterp.navigationdrawertest.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import kotlin.math.sign

/**
 * Layout to wrap a scrollable component inside a ViewPager2. Provided as a solution to the problem
 * where pages of ViewPager2 have nested scrollable elements that scroll in the same direction as
 * ViewPager2. The scrollable element needs to be the immediate and only child of this host layout.
 *
 * This solution has limitations when using multiple levels of nested scrollable elements
 * (e.g. a horizontal RecyclerView in a vertical RecyclerView in a horizontal ViewPager2).
 */
class NestedScrollableHost : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private val vTacker by lazy {
        VelocityTracker.obtain()
    }
    private var touchSlop = 0
    private var initialX = 0f
    private var initialY = 0f
    private val parentViewPager: ViewPager2?
        get() {
            var v: View? = parent as? View
            while (v != null && v !is ViewPager2) {
                v = v.parent as? View
            }
            return v as? ViewPager2
        }

    private val child: View? get() = if (childCount > 0) getChildAt(0) else null
    private val childLayoutManager: ViewPagerLayoutManager
        get() = (child as RecyclerView).layoutManager as ViewPagerLayoutManager

    init {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    private fun canChildScroll(orientation: Int, delta: Float): Boolean {
        val direction = -delta.sign.toInt()
        return when (orientation) {
            0 -> child?.canScrollHorizontally(direction) ?: false
            1 -> {
                child?.canScrollVertically(direction) ?: false
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        handleInterceptTouchEvent(ev)
        return false
    }

    private fun handleInterceptTouchEvent(e: MotionEvent) {
        val orientation = parentViewPager?.orientation ?: return
        // Early return if child can't scroll in same direction as parent
        if (!canChildScroll(orientation, -1f) && !canChildScroll(orientation, 1f)) {
            return
        }
        if (e.action == MotionEvent.ACTION_DOWN) {
            initialX = e.x
            initialY = e.y
            parent?.requestDisallowInterceptTouchEvent(true)
        } else if (e.action == MotionEvent.ACTION_MOVE) {
            vTacker.addMovement(e)
            val dx = e.x - initialX
            val dy = e.y - initialY

            val isNext = dy < 0
            val pos = childLayoutManager.pos
            val count = childLayoutManager.itemCount
            Log.e("petterp", "触发抉择")
            if (isNext && pos + 1 == count) {
                Log.e("petterp", "当前是否下滑,[$isNext], 已经滑动不了")
                parent?.requestDisallowInterceptTouchEvent(false)
                return
            }

            if (!isNext && pos == 0) {
                Log.e("petterp", "当前是否下滑,[$isNext], 已经滑动不了")
                parent?.requestDisallowInterceptTouchEvent(false)
                return
            }
            Log.e("petterp", "正常滑动,当前下标---$pos")
            parent?.requestDisallowInterceptTouchEvent(true)

//            Log.e("petterp", "---$dy")
//            val isVpHorizontal = orientation == ORIENTATION_HORIZONTAL
//            if (canChildScroll(orientation, if (isVpHorizontal) dx else dy)) {
//                Log.e("petterp", "孩子可以滚动--vp方向:$isVpHorizontal")
//                // Child can scroll, disallow all parents to intercept
//                parent?.requestDisallowInterceptTouchEvent(true)
//            } else {
//                Log.e("petterp", "当前位置：-${childLayoutManager.mCurrentSelectedPosition}")
// //                val currentItem = parentViewPager?.currentItem
// //                parentViewPager.setCurrentItem()
//                parent?.requestDisallowInterceptTouchEvent(false)
//            }
        }
    }
}
