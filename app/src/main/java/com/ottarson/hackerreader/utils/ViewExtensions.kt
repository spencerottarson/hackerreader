package com.ottarson.hackerreader.utils

import android.content.Context
import android.view.View

fun View.setVisibleOrGone(boolean: Boolean) {
    if (boolean) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun Int.dp(context: Context): Int {
    return (this * context.resources.displayMetrics.density).toInt()
}
