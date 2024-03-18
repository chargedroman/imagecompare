package com.roman.imagecompare

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView

/**
 *
 * Author: romanvysotsky
 * Created: 18.03.24
 */

@SuppressLint("ClickableViewAccessibility")
class ImageCompareView: View, ImageCompare {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        imageA = ImageView(context)
        imageB = ImageView(context)
        args = ImageCompareViewArgs(context, attrs)
    }

    private val imageA: ImageView
    private val imageB: ImageView
    private val args: ImageCompareViewArgs

    private val presenter: ImageComparePresenter = ImageComparePresenter(this)


    fun getImageViewA(): ImageView = imageA

    fun getImageViewB(): ImageView = imageB


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val splitWidth = presenter.getSplitWidth(canvas)

        drawA(canvas, splitWidth)
        drawB(canvas, splitWidth)
        drawSlider(canvas)
    }


    private fun drawA(canvas: Canvas, splitWidth: Int) {
        canvas.save()
        canvas.clipRect(0, 0, splitWidth, height)
        imageA.draw(canvas)
        canvas.restore()
    }

    private fun drawB(canvas: Canvas, splitWidth: Int) {
        canvas.save()
        canvas.clipRect(splitWidth, 0, width, height)
        imageB.draw(canvas)
        canvas.restore()
    }

    private fun drawSlider(canvas: Canvas) {
        canvas.save()
        presenter.onDrawSlider(canvas)
        canvas.restore()
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        presenter.onTouch(event)
        return true
    }

    override fun notifyChanged() {
        postInvalidate()
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Measure the ImageViews to match the size of the canvas
        imageA.measure(MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY))
        imageB.measure(MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        // Layout the ImageViews to match the size of the canvas
        imageA.layout(0, 0, measuredWidth, measuredHeight)
        imageB.layout(0, 0, measuredWidth, measuredHeight)
    }

}
