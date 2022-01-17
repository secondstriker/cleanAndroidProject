package com.codewithmohsen.lastnews.binding

import android.app.Activity
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * Binding adapters that work with an activity instance.
 */
class ImageBindingAdapters(private val activity: Activity) {
    @BindingAdapter(value = ["imageUrl"], requireAll = false)
    fun bindImage(imageView: ImageView, url: String?) {
        Glide.with(activity).load(url).into(imageView)
    }
}

