package com.roman.imagecompare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider

/**
 *
 * Author: romanvysotsky
 * Created: 18.03.24
 */

class MainActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<ImageCompareView>(R.id.view_compare)
        val slider = findViewById<Slider>(R.id.slider)
        view.bindSlider(slider)

        view.setImagesAsync(
            "https://pictures.and-charge.com/static-img/sample/location_full_3.jpg",
            "https://pictures.and-charge.com/static-img/sample/location_full_4.jpg",
        )
    }


}
