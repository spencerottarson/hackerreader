package com.ottarson.hackerreader.ui.newslist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.ottarson.hackerreader.R
import com.ottarson.hackerreader.utils.setVisibleOrGone

class NewsListAdapter(
    context: Context,
    val resourse: Int = R.layout.item_news_list
) : ArrayAdapter<StoryViewObject>(context, resourse) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                resourse,
                parent,
                false
            )
        }

        val title = view?.findViewById<TextView>(R.id.itemNewsListTitle)
        val subtitle = view?.findViewById<TextView>(R.id.itemNewsListSubtitle)
        val domain = view?.findViewById<TextView>(R.id.itemNewsListDomain)
        val comments = view?.findViewById<TextView>(R.id.itemNewsListNumComments)

        title?.text = getItem(position)?.title
        subtitle?.text = getItem(position)?.subtitle
        domain?.text = getItem(position)?.domain
        domain?.setVisibleOrGone(!getItem(position)?.domain.isNullOrBlank())
        comments?.text = getItem(position)?.numComments

        return view!!
    }
}
