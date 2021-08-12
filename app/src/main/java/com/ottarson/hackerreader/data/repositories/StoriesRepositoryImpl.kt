package com.ottarson.hackerreader.data.repositories

import com.ottarson.hackerreader.data.models.Story
import com.ottarson.hackerreader.data.network.StoriesService
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import kotlin.math.min

class StoriesRepositoryImpl @Inject constructor(
    private val storiesService: StoriesService
) : StoriesRepository {
    private var storyIds: List<Int>? = null

    override fun getTopStories(
        startIndex: Int,
        count: Int,
        sort: String,
        forceRefresh: Boolean
    ): Observable<Story> {

        return getStoryIds(sort, forceRefresh).concatMapEager { ids ->
            if (startIndex in 0..ids.count()) {
                Observable.fromIterable(
                    ids.subList(
                        startIndex,
                        min(startIndex + count, ids.count())
                    )
                ).concatMapEager { id ->
                    storiesService.getStory(id)
                }
            } else {
                Observable.empty()
            }
        }
    }

    override fun getStory(id: Int): Observable<Story> {
        return storiesService.getStory(id)
    }

    private fun getStoryIds(sort: String, forceRefresh: Boolean = false): Observable<List<Int>> {
        return if (forceRefresh || storyIds.isNullOrEmpty()) {
            storiesService.getStories(sort).doOnNext { storyIds = it }
        } else {
            Observable.just(storyIds)
        }
    }
}
