package com.roman.imagecompare.components

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.roman.imagecompare.contract.ImageCompareBaseView

/**
 *
 * Author: romanvysotsky
 * Created: 18.03.24
 */

class ImageCompareBackground(private val view: ImageCompareBaseView) {

    private val rect = Rect()
    private val backgroundPaint = Paint().apply {
        color = view.getContext().getColor(android.R.color.darker_gray)
        style = Paint.Style.FILL
    }


    fun onDrawBackground(canvas: Canvas) {
        canvas.save()
        rect.left = 0
        rect.top = 0
        rect.right = view.getWidth()
        rect.bottom = view.getHeight()
        canvas.drawRect(rect, backgroundPaint)
        canvas.restore()
    }


}
