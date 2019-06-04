package com.ponomarenko.livestreaming.ui.adapter

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

class VerticalViewPager : ViewPager {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        setPageTransformer(true, VerticalPage())
        overScrollMode = View.OVER_SCROLL_NEVER
    }

    private fun getIntercambioXY(ev: MotionEvent): MotionEvent {
        val width = width
        val height = height

        val newX = (ev.y / height) * width
        val newY = (ev.x / width) * height

        ev.setLocation(newX, newY)
        return ev
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (ev == null) return false

        val intercepted = super.onInterceptTouchEvent(getIntercambioXY(ev))
        getIntercambioXY(ev)
        return intercepted
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return super.onTouchEvent(ev?.let { getIntercambioXY(it) })
    }

    private class VerticalPage : PageTransformer {
        override fun transformPage(page: View, position: Float) {
            when {
                position < -1 -> page.alpha = 0f
                position <= 1 -> {
                    page.alpha = 1f
                    page.translationY = page.width * -position
                    val yPosition = position * page.height
                    page.translationY = yPosition
                }
                else -> page.alpha = 0f
            }

        }

    }


}
