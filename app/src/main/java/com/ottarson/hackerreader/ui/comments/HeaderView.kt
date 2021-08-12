package com.ottarson.hackerreader.ui.comments

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ottarson.hackerreader.R
import com.ottarson.hackerreader.ui.shared.ClickableTextView

class HeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    text: CharSequence = ""
) : ConstraintLayout(context, attrs, defStyle) {

    init {
        val layoutInflator = LayoutInflater.from(context)
        layoutInflator.inflate(R.layout.item_story_header, this, true)

        val textView = findViewById<ClickableTextView>(R.id.storyHeader)
        textView.text = text
    }
}
