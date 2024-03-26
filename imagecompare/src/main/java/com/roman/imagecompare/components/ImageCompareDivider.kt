package com.roman.imagecompare.components

import android.graphics.Canvas
import android.graphics.Rect
import android.view.MotionEvent
import com.roman.imagecompare.contract.ImageCompareDividerView

/**
 *
 * Author: romanvysotsky
 * Created: 18.03.24
 */

class ImageCompareDivider(private val view: ImageCompareDividerView) {

    var onPercentChangedListener: ((Float) -> Unit)? = null

    private val dividerRect = Rect()

    private var wasInitialized = false
    private var currentSplitWidth = 0
    private var currentSplitPercent = 50f


    fun notifySliderValueChanged(value: Float) {
        this.currentSplitPercent = value
        this.currentSplitWidth = calculateSplitWidth(value)
        view.onSliderPositionChanged()
    }

    fun notifyTouchValueChanged(touchX: Float) {
        this.currentSplitWidth = touchX.toInt()
        this.currentSplitPercent = calculateSplitPercent(this.currentSplitWidth)
        view.onSliderPositionChanged()
        onPercentChangedListener?.invoke(currentSplitPercent)
    }


    fun onTouchEvent(event: MotionEvent) {
        val action = event.action and MotionEvent.ACTION_MASK
        val isActionDown = action == MotionEvent.ACTION_DOWN
        val isActionMove = action == MotionEvent.ACTION_MOVE

        if (isActionDown || isActionMove) {
            notifyTouchValueChanged(event.x)
        }
    }


    fun getSplitWidth(): Int {
        if (wasInitialized.not()) {
            wasInitialized = true
            notifySliderValueChanged(currentSplitPercent)
        }

        return currentSplitWidth
    }


    fun onDrawDivider(canvas: Canvas) {
        canvas.save()

        val center = getSplitWidth()

        val iconRadius = view.getIconRadius()
        val dividerRadius = iconRadius / 8

        dividerRect.top = 0
        dividerRect.bottom = view.getHeight()
        dividerRect.left = (center - dividerRadius)
            .coerceAtLeast(0)
            .coerceAtMost(view.getWidth() - dividerRadius)
        dividerRect.right = (center + dividerRadius)
            .coerceAtLeast(0 + dividerRadius)
            .coerceAtMost(view.getWidth())

        canvas.drawRect(dividerRect, view.getArgs().dividerPaint)

        canvas.restore()
    }


    private fun calculateSplitWidth(currentSplitPercent: Float): Int {
        val percent = currentSplitPercent
            .coerceAtLeast(0f)
            .coerceAtMost(100f) / 100

        return (view.getWidth() * percent).toInt()
    }

    private fun calculateSplitPercent(currentWidth: Int): Float {
        val totalWidth = view.getWidth()
        val percent = currentWidth / (totalWidth / 100).toFloat()
        return percent
            .coerceAtLeast(0f)
            .coerceAtMost(100f)
    }


}
