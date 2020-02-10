package com.ottarson.hackerreader.ui.shared

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class NoFocusableLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {
    override fun hasFocusable(): Boolean {
        return false
    }
}
