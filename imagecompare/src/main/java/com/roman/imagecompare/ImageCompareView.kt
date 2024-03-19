package com.roman.imagecompare

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.roman.imagecompare.components.ImageCompareBackground
import com.roman.imagecompare.components.ImageCompareDrawables
import com.roman.imagecompare.components.ImageCompareSlider
import com.roman.imagecompare.contract.ImageCompareDrawablesView
import com.roman.imagecompare.contract.ImageCompareSliderView

/**
 *
 * Author: romanvysotsky
 * Created: 18.03.24
 */

@SuppressLint("ClickableViewAccessibility")
class ImageCompareView: View, ImageCompareSliderView, ImageCompareDrawablesView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        args = ImageCompareViewArgs(context, attrs)
    }

    private val args: ImageCompareViewArgs

    private val background: ImageCompareBackground = ImageCompareBackground(this)
    private val drawables: ImageCompareDrawables = ImageCompareDrawables(this)
    private val slider: ImageCompareSlider = ImageCompareSlider(this)



    fun setImages(old: Drawable, new: Drawable) {
        drawables.setImages(old, new)
        onDrawablesChanged()
    }

    fun setImagesAsync(oldUrl: String, newUrl: String) {
        drawables.setImagesAsync(oldUrl, newUrl)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val splitWidth = slider.getSplitWidth()

        background.onDrawBackground(canvas)
        drawables.onDrawImages(canvas, splitWidth)
        slider.onDrawSlider(canvas)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        slider.onTouch(event)
        return true
    }


    override fun onSliderPositionChanged() {
        invalidate()
    }

    override fun onDrawablesChanged() {
        requestLayout()
        invalidate()
    }

    override fun getArgs(): ImageCompareViewArgs {
        return args
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, drawables.getMaxDrawableHeight())
    }


}
