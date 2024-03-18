package com.roman.imagecompare

import android.graphics.Canvas
import android.view.MotionEvent

/**
 *
 * Author: romanvysotsky
 * Created: 18.03.24
 */

class ImageComparePresenter(val view: ImageCompare) {


    fun getSplitWidth(canvas: Canvas): Int {
        return canvas.width / 2
    }


    fun onTouch(event: MotionEvent) {

    }

    fun onDrawSlider(canvas: Canvas) {

    }


}
