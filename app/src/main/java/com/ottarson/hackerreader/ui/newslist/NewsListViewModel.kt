package com.ottarson.hackerreader.ui.newslist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ottarson.hackerreader.data.repositories.StoriesRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class NewsListViewModel(
    private val storiesRepository: StoriesRepository
) : ViewModel() {

    private val liveData = MutableLiveData<MutableList<StoryViewObject>>()
    private val liveDataLoadMore = MutableLiveData<Boolean>(false)

    private var disposable: Disposable? = null
    private var countLoaded = 0

    var sortOption: SortOption = SortOption.Top

    init {
        loadPage(sortOption, false)
    }

    fun getLiveData(): LiveData<MutableList<StoryViewObject>> {
        return liveData
    }

    fun getLiveDataShouldLoadMore(): LiveData<Boolean> {
        return liveDataLoadMore
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    fun refresh() {
        loadPage(sortOption, true)
    }

    fun loadPage(sort: SortOption = SortOption.Top, forceRefresh: Boolean = true) {
        liveData.value = mutableListOf()
        countLoaded = 0
        val needsForceRefresh = forceRefresh || this.sortOption != sort
        this.sortOption = sort
        loadMore(needsForceRefresh)
    }

    fun loadMore(forceRefresh: Boolean = false) {
        liveDataLoadMore.value = false
        disposable = storiesRepository.getTopStories(
            countLoaded,
            20,
            sortOption.apiEndpoint,
            forceRefresh
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ story ->
                countLoaded++
                liveData.value = liveData.value?.apply {
                    add(StoryViewObject(story))
                }
            }, { error ->
                Log.e(javaClass.simpleName, "Error: ", error)
                liveDataLoadMore.value = true
            }, {
                liveDataLoadMore.value = true
            })
    }
}
