package com.ottarson.hackerreader.ui.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ottarson.hackerreader.R
import kotlinx.android.synthetic.main.fragment_comments.commentListView

class CommentsFragment : Fragment() {

    private lateinit var viewModel: CommentsViewModel

    private lateinit var adapter: CommentsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comments, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(CommentsViewModel::class.java)
        adapter = CommentsListAdapter(requireContext())
        commentListView.adapter = adapter

        arguments?.getInt("STORY_ID")?.let { id ->
            viewModel.loadPage(id)
        }

        viewModel.getLiveDataComment().observe(this, Observer { comments ->
            adapter.clear()
            adapter.addAll(comments)
            adapter.notifyDataSetChanged()
        })

        viewModel.getLiveDataStory().observe(this, Observer { story ->
            (activity as? AppCompatActivity)?.supportActionBar?.title = story.title
        })

        commentListView.setOnItemClickListener { _, _, position, _ ->
            adapter.getItem(position)?.id?.let { commentId ->
                viewModel.toggleComment(commentId)
            }
        }
    }
}
