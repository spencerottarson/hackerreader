package com.ottarson.hackerreader.utils

import android.view.View

fun View.setVisibleOrGone(boolean: Boolean) {
    if (boolean) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}
