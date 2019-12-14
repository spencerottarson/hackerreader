package com.ottarson.hackerreader.ui.newslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        newsListSwipeRefresh.setOnRefreshListener {
            viewModel.loadPage()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        websiteOpener?.unbindService()
    }
}
