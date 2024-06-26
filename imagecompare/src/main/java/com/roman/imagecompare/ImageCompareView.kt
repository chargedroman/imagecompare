package com.roman.imagecompare

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val isLoaded = MutableLiveData<Boolean>(false)

    fun getIsLoaded(): LiveData<Boolean> = isLoaded


    fun bindSlider(slider: Slider) {
        slider.valueFrom = 0f
        slider.valueTo = 100f
        slider.value = 50f
        slider.addOnChangeListener { _, value, _ ->
            divider.notifySliderValueChanged(value)
        }
        divider.onPercentChangedListener = {
            slider.value = it
        }
    }


    fun setImages(old: Drawable, new: Drawable) {
        drawables.setImages(old, new)
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

    override fun onDrawablesChanged(isLoaded: Boolean) {
        requestLayout()
        invalidate()
        this.isLoaded.postValue(isLoaded)
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


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null)
            divider.onTouchEvent(event)
        return true
    }


}
