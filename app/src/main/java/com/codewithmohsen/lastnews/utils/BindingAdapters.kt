package com.codewithmohsen.lastnews.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.codewithmohsen.lastnews.repository.Status

object BindingAdapters {

    @JvmStatic
    @BindingAdapter(value = ["imageUrl", "placeHolder", "imageRequestListener"], requireAll = false)
    fun bindImage(imageView: ImageView, url: String?, placeHolder: Drawable?, listener: RequestListener<Drawable?>?) {
        Glide.with(imageView).load(url)
            .placeholder(placeHolder).listener(listener).into(imageView)
    }
}