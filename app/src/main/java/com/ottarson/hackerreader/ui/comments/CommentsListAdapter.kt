package com.ottarson.hackerreader.ui.comments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.ottarson.hackerreader.R
import com.ottarson.hackerreader.utils.dp

class CommentsListAdapter(
    context: Context,
    val resourse: Int = R.layout.item_comment
) : ArrayAdapter<CommentsViewObject>(context, resourse) {

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

        headingView?.text = getItem(position)?.heading
        bodyView?.text = getItem(position)?.text

        containerView?.setPadding(
            (getItem(position)?.depth ?: 0) * 20.dp(context) + 8.dp(context),
            8.dp(context),
            8.dp(context),
            8.dp(context)
        )

        return view!!
    }
}