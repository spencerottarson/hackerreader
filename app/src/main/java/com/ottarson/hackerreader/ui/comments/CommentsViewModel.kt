package com.ottarson.hackerreader.ui.comments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ottarson.hackerreader.data.network.ApiModule
import com.ottarson.hackerreader.data.repositories.CommentsRepository
import com.ottarson.hackerreader.data.repositories.StoriesRepository
import com.ottarson.hackerreader.ui.newslist.StoryViewObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CommentsViewModel : ViewModel() {
    private val commentsRepository = CommentsRepository(ApiModule().commentsService)
    private val storyRepository = StoriesRepository(ApiModule().storiesService)

    private var disposable: Disposable? = null

    private val liveDataComments = MutableLiveData<MutableList<CommentsViewObject>>()
    private val liveDataStory = MutableLiveData<StoryViewObject>()

    private val allComments = HashMap<Int, CommentsViewObject>()

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    fun getLiveDataComment(): MutableLiveData<MutableList<CommentsViewObject>> {
        return liveDataComments
    }

    fun getLiveDataStory(): MutableLiveData<StoryViewObject> {
        return liveDataStory
    }

    fun loadPage(id: Int, forceRefresh: Boolean = true) {
        liveDataComments.value = mutableListOf()
        disposable = storyRepository.getStory(id)
            .flatMap { story ->
                liveDataStory.postValue((StoryViewObject(story)))
                story.kids?.let { kids ->
                    commentsRepository.getComments(kids)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ comment ->
                comment.id?.let { id ->
                    val parentDepth = allComments[comment.parent]?.depth ?: -1
                    val commentViewObject = CommentsViewObject(comment, parentDepth + 1)
                    allComments[id] = commentViewObject
                    liveDataComments.value = liveDataComments.value?.apply {
                        add(commentViewObject)
                    }
                }
                Log.i(javaClass.simpleName, comment.text ?: "No comment")
            }, { error ->
                Log.e(javaClass.simpleName, "Error retrieving comments", error)
            })
    }
}
