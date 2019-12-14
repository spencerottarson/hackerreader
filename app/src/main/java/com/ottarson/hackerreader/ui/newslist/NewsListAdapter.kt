package com.ottarson.hackerreader.ui.newslist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.navigation.findNavController
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
        val comments = view?.findViewById<View>(R.id.itemNewsListComments)
        val numComments = view?.findViewById<TextView>(R.id.itemNewsListNumComments)

        val story = getItem(position)

        title?.text = story?.title
        subtitle?.text = story?.subtitle
        domain?.text = story?.domain
        domain?.setVisibleOrGone(!story?.domain.isNullOrBlank())
        numComments?.text = story?.numComments

        comments?.setOnClickListener {
            view?.findNavController()?.navigate(
                R.id.action_newsListFragment_to_commentsFragment,
                Bundle().apply { putInt("STORY_ID", story?.id ?: 0) }
            )
        }

        return view!!
    }
}
