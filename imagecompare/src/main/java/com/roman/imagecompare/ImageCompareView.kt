package com.roman.imagecompare

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target

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
        args = ImageCompareViewArgs(context, attrs)
    }

    private val args: ImageCompareViewArgs
    private val presenter: ImageComparePresenter = ImageComparePresenter(this)

    private val rect = Rect()
    private val backgroundPaint = Paint().apply {
        color = context.getColor(android.R.color.darker_gray)
        style = Paint.Style.FILL
    }
    private val separatorPaint = Paint().apply {
        color = context.getColor(android.R.color.holo_blue_light)
        style = Paint.Style.FILL
    }

    private var drawableOld: Drawable? = null
    private var drawableNew: Drawable? = null
    private var aspectRatioOld = 0f
    private var aspectRatioNew = 0f
    private var viewHeight = 0

    private val drawableOldTarget = GlideTargetAdapter {
        drawableOld = it
        onDrawablesUpdated()
    }

    private val drawableNewTarget = GlideTargetAdapter {
        drawableNew = it
        onDrawablesUpdated()
    }


    fun setImages(old: Drawable, new: Drawable) {
        drawableOld = old
        drawableNew = new
        onDrawablesUpdated()
    }

    fun setImagesAsync(oldUrl: String, newUrl: String) {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                loadImageAsync(oldUrl, drawableOldTarget)
                loadImageAsync(newUrl, drawableNewTarget)
            }
        })
    }



    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val drawableNew = drawableNew
        val drawableOld = drawableOld

        rect.left = 0
        rect.top = 0
        rect.right = width
        rect.bottom = height
        canvas.drawRect(rect, backgroundPaint)

        if (drawableNew == null || drawableOld == null) {
            return
        }

        val splitWidth = presenter.getSplitWidth(canvas)

        canvas.save()
        canvas.clipRect(0, 0, splitWidth, height)
        val adjustedHeightOld = (width * aspectRatioOld).toInt()
        val shiftOld = (height - adjustedHeightOld).coerceAtLeast(0) / 2
        drawableOld.bounds.left = 0
        drawableOld.bounds.right = width
        drawableOld.bounds.top = shiftOld
        drawableOld.bounds.bottom = adjustedHeightOld + shiftOld
        drawableOld.draw(canvas)
        canvas.restore()

        canvas.save()
        canvas.clipRect(splitWidth, 0, width, height)
        val adjustedHeightNew = (width * aspectRatioNew).toInt()
        val shiftNew = (height - adjustedHeightNew).coerceAtLeast(0) / 2
        drawableNew.bounds.left = 0
        drawableNew.bounds.right = width
        drawableNew.bounds.top = shiftNew
        drawableNew.bounds.bottom = adjustedHeightNew + shiftNew
        drawableNew.draw(canvas)
        canvas.restore()

        canvas.save()
        presenter.onDrawSlider(canvas)
        canvas.restore()
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        presenter.onTouch(event)
        return true
    }

    override fun onSliderPositionChanged() {
        postInvalidate()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, viewHeight)
    }


    private fun loadImageAsync(url: String, target: Target<Drawable>) {
        Glide
            .with(this)
            .load(url)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .format(DecodeFormat.PREFER_RGB_565)
            .into(target)
    }

    private fun calculateAspectRatio(drawable: Drawable?): Float {
        if (drawable == null)
            return 0f

        return drawable.intrinsicHeight.toFloat() / drawable.intrinsicWidth
    }

    private fun onDrawablesUpdated() {
        aspectRatioOld = calculateAspectRatio(drawableOld)
        aspectRatioNew = calculateAspectRatio(drawableNew)
        viewHeight = maxOf(
            (width * aspectRatioOld).toInt(),
            (width * aspectRatioNew).toInt()
        )
        requestLayout()
        invalidate()
    }

}
