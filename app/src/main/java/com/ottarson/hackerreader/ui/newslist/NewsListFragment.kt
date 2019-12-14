package com.ottarson.hackerreader.ui.newslist

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ottarson.hackerreader.R
import com.ottarson.hackerreader.ui.shared.WebsiteOpener
import kotlinx.android.synthetic.main.fragment_news_list.*

class NewsListFragment : Fragment() {

    private lateinit var viewModel: NewsListViewModel

    private lateinit var adapter: NewsListAdapter

    private var websiteOpener: WebsiteOpener? = null

    private var shouldLoadMore = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_news_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NewsListViewModel::class.java)

        adapter = NewsListAdapter(requireContext())
        newsListView.adapter = adapter

        newsListSwipeRefresh.isRefreshing = true

        websiteOpener = WebsiteOpener(requireContext())

        newsListView.setOnItemClickListener { _, _, position, _ ->
            adapter.getItem(position)?.url?.let { url ->
                websiteOpener?.launch(url)
            }
        }

        viewModel.getLiveData().observe(this, Observer<List<StoryViewObject>> { stories ->
            newsListSwipeRefresh.isRefreshing = false
            adapter.clear()
            adapter.addAll(stories)
            adapter.notifyDataSetChanged()
            websiteOpener?.setPotentialUrls(stories.mapNotNull { it.url })
        })

        viewModel.getLiveDataShouldLoadMore().observe(this, Observer { shouldLoadMore = it })

        newsListSwipeRefresh.setOnRefreshListener {
            viewModel.loadPage()
        }

        setupInfiniteScroll()
    }

    override fun onDestroy() {
        super.onDestroy()
        websiteOpener?.unbindService()
    }

    private fun setupInfiniteScroll() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            newsListView.setOnScrollChangeListener { _, _, _, _, _ ->
                if (shouldLoadMore()) {
                    viewModel.loadMore()
                }
            }
        } else {
            newsListView.addFooterView(Button(requireContext()).apply {
                text = getString(R.string.load_more)
                setOnClickListener {
                    viewModel.loadMore()
                }
            })
        }
    }

    private fun shouldLoadMore(): Boolean {
        return shouldLoadMore && newsListView.lastVisiblePosition == newsListView.adapter.count - 2
    }
}
