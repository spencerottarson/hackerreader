package com.ottarson.hackerreader.utils

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import com.ottarson.hackerreader.HackerReaderApplication
import com.ottarson.hackerreader.HackerReaderComponent

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

fun Fragment.getInjector(): HackerReaderComponent {
    return (activity?.application as? HackerReaderApplication)?.component!!
}

fun View.expand(scrollView: ScrollView? = null, onComplete: (() -> Unit)? = null) {
    this.measure(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    val targetHeight = this.measuredHeight

    // Older versions (pre API 21) cancel animations for views with a height of 0.
    this.layoutParams.height = 1
    this.visibility = View.VISIBLE
    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            this@expand.layoutParams.height = if (interpolatedTime == 1f) {
                onComplete?.invoke()
                LinearLayout.LayoutParams.WRAP_CONTENT
            } else {
                (targetHeight * interpolatedTime).toInt()
            }
            this@expand.requestLayout()
            scrollView?.post {
                scrollView.smoothScrollTo(0, scrollView.bottom)
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // 1dp/ms
    animation.duration =
        (targetHeight / this.context.resources.displayMetrics.density).toLong()

    this.startAnimation(animation)
}

fun View.collapse(onComplete: (() -> Unit)? = null) {
    val initialHeight = this.measuredHeight

    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            if (interpolatedTime == 1f) {
                this@collapse.visibility = View.GONE
                onComplete?.invoke()
            } else {
                this@collapse.layoutParams.height =
                    initialHeight - (initialHeight * interpolatedTime).toInt()
                this@collapse.requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // 1dp/ms
    animation.duration =
        (initialHeight / this.context.resources.displayMetrics.density).toLong()
    this.startAnimation(animation)
}
