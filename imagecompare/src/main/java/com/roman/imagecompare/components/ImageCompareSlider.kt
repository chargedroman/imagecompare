package com.roman.imagecompare.components

import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import com.roman.imagecompare.contract.ImageCompareSliderView

/**
 *
 * Author: romanvysotsky
 * Created: 18.03.24
 */

class ImageCompareSlider(private val view: ImageCompareSliderView) {

    private val separatorPaint = Paint().apply {
        color = view.getContext().getColor(android.R.color.holo_blue_light)
        style = Paint.Style.FILL
    }


    fun getSplitWidth(): Int {
        return view.getWidth() / 2
    }


    fun onTouch(event: MotionEvent) {

    }

    fun onDrawSlider(canvas: Canvas) {
        canvas.save()
        //draw
        canvas.restore()
    }


}
