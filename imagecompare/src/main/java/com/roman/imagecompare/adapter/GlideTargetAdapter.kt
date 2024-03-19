package com.roman.imagecompare.adapter

import android.graphics.drawable.Drawable
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition

/**
 *
 * Author: romanvysotsky
 * Created: 19.03.24
 */

class GlideTargetAdapter(
    private val setter: (Drawable?) -> Unit
): SimpleTarget<Drawable>() {

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        setter.invoke(resource)
    }

}
