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

class CommentsViewModel(
    private val storyRepository: StoriesRepository,
    private val commentsRepository: CommentsRepository
) : ViewModel() {

    private var disposable: Disposable? = null

    private val liveDataComments = MutableLiveData<MutableList<CommentsViewObject>>()
    private val liveDataStory = MutableLiveData<StoryViewObject>()

    private val commentMap = HashMap<Int, CommentsViewObject>()
    private val allComments = mutableListOf<CommentsViewObject>()

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
                    val parentDepth = commentMap[comment.parent]?.depth ?: -1
                    val commentViewObject = CommentsViewObject(comment, parentDepth + 1)
                    commentViewObject.hidden =
                        commentMap[comment.parent]?.hidden == true ||
                                commentMap[comment.parent]?.collapsed == true

                    commentMap[id] = commentViewObject
                    allComments.add(commentViewObject)
                    liveDataComments.value = allComments.filter { !it.hidden }.toMutableList()
                }
                Log.i(javaClass.simpleName, comment.text ?: "No comment")
            }, { error ->
                Log.e(javaClass.simpleName, "Error retrieving comments", error)
            })
    }

    fun toggleComment(id: Int) {
        commentMap[id]?.collapsed = commentMap[id]?.collapsed?.not() ?: false
        toggleCommentChildren(commentMap[id]?.childIds)

        liveDataComments.value = allComments.filter { !it.hidden }.toMutableList()
    }

    private fun toggleCommentChildren(ids: List<Int>?) {
        ids?.forEach { id ->
            commentMap[id]?.hidden = commentMap[id]?.hidden?.not() ?: false
            if (commentMap[id]?.collapsed == false) {
                toggleCommentChildren(commentMap[id]?.childIds)
            }
        }
    }
}
