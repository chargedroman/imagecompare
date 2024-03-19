package com.roman.imagecompare.components

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.roman.imagecompare.contract.ImageCompareDividerView

/**
 *
 * Author: romanvysotsky
 * Created: 18.03.24
 */

class ImageCompareDivider(private val view: ImageCompareDividerView) {

    private val dividerRect = Rect()
    private var currentValue = 50f

    private val dividerPaint = Paint().apply {
        color = view.getContext().getColor(android.R.color.holo_blue_light)
        style = Paint.Style.FILL
    }


    fun notifySliderValueChanged(value: Float) {
        this.currentValue = value
        view.onSliderPositionChanged()
    }


    fun getSplitWidth(): Int {
        val percent = currentValue
            .coerceAtLeast(0f)
            .coerceAtMost(100f) / 100

        return (view.getWidth() * percent).toInt()
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

        canvas.drawRect(dividerRect, dividerPaint)

        canvas.restore()
    }


}
