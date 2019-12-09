package com.ottarson.hackerreader.ui.newslist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ottarson.hackerreader.data.network.ApiModule
import com.ottarson.hackerreader.data.repositories.StoriesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class NewsListViewModel : ViewModel() {
    private val storiesRepository = StoriesRepository(ApiModule().storiesService)

    private val liveData = MutableLiveData<MutableList<StoryViewObject>>()

    private var disposable: Disposable? = null

    init {
        loadPage(false)
    }

    fun getLiveData(): LiveData<MutableList<StoryViewObject>> {
        return liveData
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    fun loadPage(forceRefresh: Boolean = true) {
        liveData.value = mutableListOf()
        disposable = storiesRepository.getTopStories(forceRefresh = forceRefresh)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ story ->
                liveData.value = liveData.value?.apply {
                    add(StoryViewObject(story))
                }
            }, { error ->
                Log.e(javaClass.simpleName, "Error: ", error)
            })
    }
}
