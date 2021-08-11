package com.ottarson.hackerreader.ui.newslist

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.ottarson.hackerreader.R
import com.ottarson.hackerreader.ui.shared.ItemType
import com.ottarson.hackerreader.ui.shared.ViewModelFactory
import com.ottarson.hackerreader.ui.shared.WebsiteOpener
import com.ottarson.hackerreader.utils.getInjector
import com.ottarson.hackerreader.utils.inDarkMode
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_news_list.*

class NewsListFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory

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
        getInjector().inject(this)
        viewModel = ViewModelProviders.of(
            this,
            viewModelFactory
        ).get(NewsListViewModel::class.java)

        setHasOptionsMenu(true)

        adapter = NewsListAdapter(requireContext())
        newsListView.adapter = adapter

        newsListSwipeRefresh.isRefreshing = true

        websiteOpener = WebsiteOpener(requireContext())

        newsListView.setOnItemClickListener { _, _, position, _ ->
            adapter.getItem(position)?.let { onItemClick(it) }
        }

        viewModel.getLiveData().observe(viewLifecycleOwner, { stories ->
            newsListSwipeRefresh.isRefreshing = false
            adapter.clear()
            adapter.addAll(stories)
            adapter.notifyDataSetChanged()
            websiteOpener?.setPotentialUrls(stories.mapNotNull { it.url })
        })

        viewModel.getLiveDataShouldLoadMore().observe(viewLifecycleOwner, { shouldLoadMore = it })

        newsListSwipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }

        setupInfiniteScroll()
    }

    override fun onDestroy() {
        super.onDestroy()
        websiteOpener?.unbindService()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (context?.inDarkMode() == true) {
            inflater.inflate(R.menu.menu_news_list_dark, menu)
        } else {
            inflater.inflate(R.menu.menu_news_list_light, menu)
        }

        val subMenu = menu.findItem(R.id.menu_sort).subMenu

        SortOption.values().forEach { sortOption ->
            subMenu.add(
                R.id.menu_group_sort,
                sortOption.menuItem,
                sortOption.order,
                sortOption.displayName
            ).isCheckable = true
        }

        subMenu.setGroupCheckable(R.id.menu_group_sort, true, true)

        menu.findItem(viewModel.sortOption.menuItem).isChecked = true

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_sort) {
            return true
        }

        SortOption.values().forEach { sortOption ->
            if (item.itemId == sortOption.menuItem) {
                if (!item.isChecked) {
                    item.isChecked = true
                    viewModel.loadPage(sortOption)
                }

                return true
            }
        }

        return super.onOptionsItemSelected(item)
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

    private fun onItemClick(item: StoryViewObject) {
        if (item.type == ItemType.Story) {
            item.url?.let { url ->
                websiteOpener?.launch(url)
            } ?: run {
                navigateToComments(item.id)
            }
        }
    }

    private fun navigateToComments(storyId: Int?) {
        view?.findNavController()?.navigate(
            R.id.action_newsListFragment_to_commentsFragment,
            Bundle().apply { putInt("STORY_ID", storyId ?: 0) }
        )
    }
}
