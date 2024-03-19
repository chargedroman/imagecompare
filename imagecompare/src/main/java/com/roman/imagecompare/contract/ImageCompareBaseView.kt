package com.roman.imagecompare.contract

import android.content.Context
import android.view.ViewTreeObserver
import com.roman.imagecompare.ImageCompareViewArgs

/**
 *
 * Author: romanvysotsky
 * Created: 18.03.24
 */

interface ImageCompareBaseView {

    fun getArgs(): ImageCompareViewArgs

    fun getContext(): Context
    fun getViewTreeObserver(): ViewTreeObserver
    fun getHeight(): Int
    fun getWidth(): Int

}
