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
import com.ottarson.hackerreader.ui.shared.ViewModelFactory
import com.ottarson.hackerreader.utils.getInjector
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_comments.commentListView
import kotlinx.android.synthetic.main.fragment_comments.commentListViewNextButton
import kotlinx.android.synthetic.main.fragment_comments.commentListViewPreviousButton

class CommentsFragment : Fragment(), CommentInteractionDelegate {

    @Inject lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: CommentsViewModel

    private lateinit var adapter: CommentsListAdapter

    private var headerView: HeaderView? = null

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
        getInjector().inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CommentsViewModel::class.java)
        adapter = CommentsListAdapter(requireContext(), delegate = this)
        commentListView.adapter = adapter

        arguments?.getInt("STORY_ID")?.let { id ->
            viewModel.loadPage(id)
        }

        viewModel.getLiveDataComment().observe(viewLifecycleOwner, Observer { comments ->
            adapter.clear()
            adapter.addAll(comments)
            adapter.notifyDataSetChanged()
        })

        viewModel.getLiveDataStory().observe(viewLifecycleOwner, Observer { story ->
            (activity as? AppCompatActivity)?.supportActionBar?.title = story.title

            story.text?.let {
                headerView = HeaderView(requireContext(), text = it)
                commentListView.addHeaderView(headerView)
            }
        })

        commentListViewNextButton.setOnClickListener {
            commentListView.smoothScrollToPositionFromTop(
                viewModel.getNextTopLevelCommentPosition(commentListView.firstVisiblePosition + commentListView.headerViewsCount) + 1 + commentListView.headerViewsCount,
                0,
                800
            )
        }

        commentListViewPreviousButton.setOnClickListener {
            commentListView.smoothScrollToPositionFromTop(
                viewModel.getPreviousTopLevelCommentPosition(commentListView.firstVisiblePosition - commentListView.headerViewsCount) + commentListView.headerViewsCount,
                0,
                800
            )
        }
    }

    override fun onItemClicked(commentId: Int) {
        viewModel.toggleComment(commentId)
    }
}
