package com.ottarson.hackerreader.ui.shared

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.method.Touch
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

class ClickableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : TextView(context, attrs, defStyle) {

    private var linkHit = false

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        val spannableString = SpannableString(text)

        val matcher = Patterns.WEB_URL.matcher(spannableString)

        while (matcher.find()) {
            val start = matcher.start()
            val end = matcher.end()

            spannableString.setSpan(
                CustomClickableSpan(context, matcher.group()),
                start,
                end,
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
            ds.color = Color.CYAN
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
            val action = event.action
            if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN
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
                return if (link.size != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget)
                    } else if (action == MotionEvent.ACTION_DOWN) {
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
