package com.roman.imagecompare

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.google.android.material.slider.Slider
import com.roman.imagecompare.components.ImageCompareBackground
import com.roman.imagecompare.components.ImageCompareDivider
import com.roman.imagecompare.components.ImageCompareDrawables
import com.roman.imagecompare.contract.ImageCompareDividerView
import com.roman.imagecompare.contract.ImageCompareDrawablesView

/**
 *
 * Author: romanvysotsky
 * Created: 18.03.24
 */

@SuppressLint("ClickableViewAccessibility")
class ImageCompareView: View, ImageCompareDividerView, ImageCompareDrawablesView {

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
    private val divider: ImageCompareDivider = ImageCompareDivider(this)


    fun bindSlider(slider: Slider) {
        slider.valueFrom = 0f
        slider.valueTo = 100f
        slider.value = 50f
        slider.addOnChangeListener { _, value, _ ->
            divider.notifySliderValueChanged(value)
        }
    }


    fun setImages(old: Drawable, new: Drawable) {
        drawables.setImages(old, new)
        onDrawablesChanged()
    }

    fun setImagesAsync(oldUrl: String, newUrl: String) {
        drawables.setImagesAsync(oldUrl, newUrl)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val splitWidth = divider.getSplitWidth()

        background.onDrawBackground(canvas)
        drawables.onDrawImages(canvas, splitWidth)
        divider.onDrawDivider(canvas)
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

    override fun getIconRadius(): Int {
        return width / 32
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, drawables.getMaxDrawableHeight())
    }


}
