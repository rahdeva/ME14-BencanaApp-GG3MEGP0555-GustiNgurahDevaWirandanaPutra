package com.rahdeva.bencanaapp.utils.provider

import android.content.Context
import android.graphics.drawable.Drawable


class ResourceProvider(private val context: Context) {
    fun getString(resourceId: Int): String {
        return context.getString(resourceId)
    }
    fun getDrawable(resourceId: Int): Drawable {
        return context.getDrawable(resourceId) as Drawable
    }
}