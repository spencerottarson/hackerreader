package com.ottarson.hackerreader.ui.comments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import com.ottarson.hackerreader.R
import com.ottarson.hackerreader.ui.newslist.NewsListViewModel
import kotlinx.android.synthetic.main.fragment_comments.commentsStoryId

class CommentsFragment : Fragment() {

    private lateinit var viewModel: CommentsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comments, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(CommentsViewModel::class.java)

        arguments?.getInt("STORY_ID")?.let { id ->
            commentsStoryId.text = "$id"
            viewModel.loadPage(id)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
