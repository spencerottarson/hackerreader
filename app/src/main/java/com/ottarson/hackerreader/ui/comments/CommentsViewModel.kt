package com.ottarson.hackerreader.ui.comments

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ottarson.hackerreader.data.network.ApiModule
import com.ottarson.hackerreader.data.repositories.CommentsRepository
import com.ottarson.hackerreader.data.repositories.StoriesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CommentsViewModel : ViewModel() {
    private val commentsRepository = CommentsRepository(ApiModule().commentsService)
    private val storyRepository = StoriesRepository(ApiModule().storiesService)

    private var disposable: Disposable? = null

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    fun loadPage(id: Int, forceRefresh: Boolean = true) {
        disposable = storyRepository.getStory(id)
            .flatMap { story ->
                story.kids?.let { kids ->
                    commentsRepository.getComments(kids)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ comment ->
                Log.i(javaClass.simpleName, comment.text ?: "No comment")
            }, { error ->
                Log.e(javaClass.simpleName, "Error retrieving comments", error)
            })
    }
}
