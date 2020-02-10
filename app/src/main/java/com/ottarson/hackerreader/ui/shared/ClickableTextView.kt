package com.ottarson.hackerreader.ui.shared

import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.method.Touch
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ottarson.hackerreader.R

class ClickableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : TextView(context, attrs, defStyle) {

    private var linkHit = false

    override fun setText(text: CharSequence?, type: BufferType?) {
        if (text is Spanned) {
            setText(text, type)
        } else {
            super.setText(text, type)
        }
    }

    private fun setText(text: Spanned?, type: BufferType?) {
        val spannableString = SpannableString(text)

        val spans = text?.getSpans(0, text.length, URLSpan::class.java)

        spans?.forEach { span ->
            spannableString.setSpan(
                CustomClickableSpan(context, span.url),
                spannableString.getSpanStart(span),
                spannableString.getSpanEnd(span),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        super.setText(spannableString, type)
        this.movementMethod = LocalLinkMovementMethod.getInstance()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        linkHit = false
        super.onTouchEvent(event)
        return linkHit
    }

    class CustomClickableSpan(
        private val context: Context,
        private val link: String
    ) : ClickableSpan() {
        override fun onClick(widget: View) {
            val opener = WebsiteOpener(context)
            opener.launch(Uri.parse(link))
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            val mode = context.resources?.configuration?.uiMode
                ?.and(Configuration.UI_MODE_NIGHT_MASK)

            ds.color = when (mode) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    ContextCompat.getColor(context, R.color.linkColorDarkMode)
                }
                else -> {
                    ContextCompat.getColor(context, R.color.linkColor)
                }
            }
            ds.isUnderlineText = true
        }
    }

    class LocalLinkMovementMethod : LinkMovementMethod() {
        companion object {
            private var instance: LocalLinkMovementMethod? = null

            fun getInstance(): LinkMovementMethod {
                if (instance == null) {
                    instance = LocalLinkMovementMethod()
                }
                return instance!!
            }
        }

        override fun onTouchEvent(
            widget: TextView,
            buffer: Spannable,
            event: MotionEvent
        ): Boolean {
            if (event.action == MotionEvent.ACTION_UP ||
                event.action == MotionEvent.ACTION_DOWN
            ) {
                var x = event.x.toInt()
                var y = event.y.toInt()
                x -= widget.totalPaddingLeft
                y -= widget.totalPaddingTop
                x += widget.scrollX
                y += widget.scrollY
                val layout = widget.layout
                val line = layout.getLineForVertical(y)
                val off = layout.getOffsetForHorizontal(line, x.toFloat())
                val link = buffer.getSpans(
                    off, off, CustomClickableSpan::class.java
                )
                return if (link.isNotEmpty()) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget)
                    } else if (event.action == MotionEvent.ACTION_DOWN) {
                        Selection.setSelection(
                            buffer,
                            buffer.getSpanStart(link[0]),
                            buffer.getSpanEnd(link[0])
                        )
                    }
                    (widget as? ClickableTextView)?.linkHit = true
                    true
                } else {
                    Selection.removeSelection(buffer)
                    Touch.onTouchEvent(widget, buffer, event)
                    false
                }
            }
            return Touch.onTouchEvent(widget, buffer, event)
        }
    }
}
