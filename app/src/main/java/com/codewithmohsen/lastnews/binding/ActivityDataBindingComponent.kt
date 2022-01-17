package com.codewithmohsen.lastnews.binding

import android.app.Activity
import androidx.databinding.DataBindingComponent

/**
 * A Data Binding Component implementation for activities.
 */
class ActivityDataBindingComponent(activity: Activity) : DataBindingComponent {
    private val adapter = ImageBindingAdapters(activity)
    override fun getImageBindingAdapters() = adapter
}
