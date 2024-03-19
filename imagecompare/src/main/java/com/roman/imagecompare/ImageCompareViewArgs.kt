package com.roman.imagecompare

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet

/**
 *
 * Author: romanvysotsky
 * Created: 18.03.24
 */

class ImageCompareViewArgs(context: Context, attrs: AttributeSet?) {

    val backgroundPaint = Paint().apply {
        color = context.getColor(android.R.color.darker_gray)
        style = Paint.Style.FILL
    }

    val dividerPaint = Paint().apply {
        color = context.getColor(android.R.color.holo_blue_light)
        style = Paint.Style.FILL
    }


    init {
        if (attrs != null) {
            val attributes =
                context.obtainStyledAttributes(attrs, R.styleable.ImageCompareView)

            val backgroundPaintColor =
                attributes.getColor(
                    R.styleable.ImageCompareView_backgroundColor,
                    context.getColor(android.R.color.darker_gray),
                )
            backgroundPaint.color = backgroundPaintColor

            val dividerPaintColor =
                attributes.getColor(
                    R.styleable.ImageCompareView_dividerColor,
                    context.getColor(android.R.color.holo_blue_light),
                )
            dividerPaint.color = dividerPaintColor

            attributes.recycle()
        }

    }

}
