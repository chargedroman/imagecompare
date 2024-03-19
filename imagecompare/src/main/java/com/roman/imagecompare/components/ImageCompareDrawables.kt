package com.roman.imagecompare.components

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.ViewTreeObserver
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import com.roman.imagecompare.adapter.GlideTargetAdapter
import com.roman.imagecompare.contract.ImageCompareDrawablesView

/**
 *
 * Author: romanvysotsky
 * Created: 18.03.24
 */

class ImageCompareDrawables(val view: ImageCompareDrawablesView) {


    private var drawableOld: Drawable? = null
    private var drawableNew: Drawable? = null
    private var aspectRatioOld = 0f
    private var aspectRatioNew = 0f
    private var maxDrawableHeight = 0



    private val drawableOldTarget = GlideTargetAdapter {
        drawableOld = it
        onDrawablesChanged()
    }

    private val drawableNewTarget = GlideTargetAdapter {
        drawableNew = it
        onDrawablesChanged()
    }

    fun setImages(old: Drawable, new: Drawable) {
        this.drawableOld = old
        this.drawableNew = new
        onDrawablesChanged()
    }

    fun setImagesAsync(oldUrl: String, newUrl: String) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                loadImageAsync(oldUrl, drawableOldTarget)
                loadImageAsync(newUrl, drawableNewTarget)
            }
        })
    }

    fun getMaxDrawableHeight(): Int {
        return maxDrawableHeight
    }


    fun onDrawImages(canvas: Canvas, splitWidth: Int) {
        val drawableOld = drawableOld
        val drawableNew = drawableNew
        if (drawableOld == null || drawableNew == null)
            return

        onDrawDrawableOld(canvas, drawableOld, splitWidth)
        onDrawDrawableNew(canvas, drawableNew, splitWidth)
    }


    private fun onDrawDrawableOld(canvas: Canvas, drawable: Drawable, splitWidth: Int) {
        canvas.save()
        canvas.clipRect(0, 0, splitWidth, view.getHeight())
        val adjustedHeightOld = (view.getWidth() * aspectRatioOld).toInt()
        val shiftOld = (view.getHeight() - adjustedHeightOld).coerceAtLeast(0) / 2
        drawable.bounds.left = 0
        drawable.bounds.right = view.getWidth()
        drawable.bounds.top = shiftOld
        drawable.bounds.bottom = adjustedHeightOld + shiftOld
        drawable.draw(canvas)
        canvas.restore()
    }

    private fun onDrawDrawableNew(canvas: Canvas, drawable: Drawable, splitWidth: Int) {
        canvas.save()
        canvas.clipRect(splitWidth, 0, view.getWidth(), view.getHeight())
        val adjustedHeightNew = (view.getWidth() * aspectRatioNew).toInt()
        val shiftNew = (view.getHeight() - adjustedHeightNew).coerceAtLeast(0) / 2
        drawable.bounds.left = 0
        drawable.bounds.right = view.getWidth()
        drawable.bounds.top = shiftNew
        drawable.bounds.bottom = adjustedHeightNew + shiftNew
        drawable.draw(canvas)
        canvas.restore()
    }


    private fun loadImageAsync(url: String, target: Target<Drawable>) {
        Glide
            .with(view.getContext())
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

    private fun onDrawablesChanged() {
        aspectRatioOld = calculateAspectRatio(drawableOld)
        aspectRatioNew = calculateAspectRatio(drawableNew)
        maxDrawableHeight = maxOf(
            (view.getWidth() * aspectRatioOld).toInt(),
            (view.getWidth() * aspectRatioNew).toInt()
        )
        view.onDrawablesChanged()
    }


}
