package com.ottarson.hackerreader.ui.comments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.ottarson.hackerreader.utils.dp

class CommentsListAdapter(
    context: Context,
    val resourse: Int = android.R.layout.simple_list_item_1
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

        val textView = view?.findViewById<TextView>(android.R.id.text1)
        textView?.text = getItem(position)?.text

        textView?.setPadding(
            (getItem(position)?.depth ?: 0) * 20.dp(context),
            0,
            0,
            0
        )

        return view!!
    }
}
