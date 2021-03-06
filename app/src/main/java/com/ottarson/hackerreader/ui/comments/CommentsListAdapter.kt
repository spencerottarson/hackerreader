package com.ottarson.hackerreader.ui.comments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.ottarson.hackerreader.R
import com.ottarson.hackerreader.utils.collapse
import com.ottarson.hackerreader.utils.dp
import com.ottarson.hackerreader.utils.expand
import com.ottarson.hackerreader.utils.setVisibleOrGone

class CommentsListAdapter(
    context: Context,
    val resourse: Int = R.layout.item_comment,
    val delegate: CommentInteractionDelegate? = null
) : ArrayAdapter<CommentViewObject>(context, resourse) {

    private val colors: IntArray = context.resources.getIntArray(R.array.depthColors)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                resourse,
                parent,
                false
            )
        }

        val containerView = view?.findViewById<ViewGroup>(R.id.itemComment)
        val headingView = view?.findViewById<TextView>(R.id.itemCommentHeading)
        val bodyView = view?.findViewById<TextView>(R.id.itemCommentBody)
        val colorIndicator = view?.findViewById<View>(R.id.itemCommentDepthColor)

        headingView?.text = getItem(position)?.heading
        bodyView?.text = getItem(position)?.text

        val depth = getItem(position)?.depth ?: 0

        colorIndicator?.setBackgroundColor(
            colors[depth % colors.count()]
        )

        containerView?.setPadding(
            depth * 15.dp(context) + 8.dp(context),
            8.dp(context),
            8.dp(context),
            8.dp(context)
        )

        bodyView?.setVisibleOrGone(getItem(position)?.viewState != ViewState.collapsed)

        bodyView?.visibility = if (getItem(position)?.viewState == ViewState.collapsed) {
            bodyView?.layoutParams?.height = 0
            View.GONE
        } else if (getItem(position)?.viewState == ViewState.expanded) {
            bodyView?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
            View.VISIBLE
        } else {
            View.VISIBLE
        }

        containerView?.isClickable = true

        containerView?.setOnClickListener {
            getItem(position)?.id?.let {
                if (getItem(position)?.viewState == ViewState.expanded) {
                    getItem(position)?.viewState = ViewState.collapsing
                    bodyView?.collapse {
                        getItem(position)?.viewState = ViewState.collapsed
                    }
                } else if (getItem(position)?.viewState == ViewState.collapsed) {
                    getItem(position)?.viewState = ViewState.expanding
                    bodyView?.expand {
                        getItem(position)?.viewState = ViewState.expanded
                    }
                }
                delegate?.onItemClicked(it)
            }
        }

        return view!!
    }
}
