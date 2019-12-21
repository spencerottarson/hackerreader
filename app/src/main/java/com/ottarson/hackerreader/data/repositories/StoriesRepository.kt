package com.ottarson.hackerreader.data.repositories

import com.ottarson.hackerreader.data.models.Story
import com.ottarson.hackerreader.data.network.StoriesService
import io.reactivex.Observable
import javax.inject.Inject

class StoriesRepository @Inject constructor(
    private val storiesService: StoriesService
) {
    private var storyIds: List<Int>? = null

    fun getTopStories(
        startIndex: Int = 0,
        count: Int = 20,
        forceRefresh: Boolean = false
    ): Observable<Story> {
        return getStoryIds(forceRefresh).flatMap { ids ->
            Observable.fromIterable(ids.subList(startIndex, startIndex + count)).flatMap { id ->
                storiesService.getStory(id)
            }
        }
    }

    fun getStory(id: Int): Observable<Story> {
        return storiesService.getStory(id)
    }

    private fun getStoryIds(forceRefresh: Boolean = false): Observable<List<Int>> {
        return if (forceRefresh || storyIds.isNullOrEmpty()) {
            storiesService.getStories().doOnNext { storyIds = it }
        } else {
            Observable.just(storyIds)
        }
    }
}
