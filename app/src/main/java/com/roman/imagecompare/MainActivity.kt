package com.roman.imagecompare

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

/**
 *
 * Author: romanvysotsky
 * Created: 18.03.24
 */

class MainActivity: AppCompatActivity(), RequestListener<Drawable> {


    private var imagesProcessed = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<ImageCompareView>(R.id.view_compare)
        imagesProcessed = 0

        view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                loadImages(view)
            }
        })
    }


    private fun loadImages(view: ImageCompareView) {
        Glide
            .with(this)
            .load("https://pictures.and-charge.com/static-img/sample/location_full_3.jpg")
            .format(DecodeFormat.PREFER_RGB_565)
            .listener(this)
            .into(view.getImageViewA())

        Glide
            .with(this)
            .load("https://pictures.and-charge.com/static-img/sample/location_full_4.jpg")
            .format(DecodeFormat.PREFER_RGB_565)
            .listener(this)
            .into(view.getImageViewB())
    }

    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
    ): Boolean {
        return onProcessed()
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        return onProcessed()
    }

    private fun onProcessed(): Boolean {
        imagesProcessed++

        if (imagesProcessed == 2) {
            runOnUiThread {
                findViewById<ImageCompareView>(R.id.view_compare).invalidate()
            }
            println("okhttp invalidated")
        }

        return false
    }

}
