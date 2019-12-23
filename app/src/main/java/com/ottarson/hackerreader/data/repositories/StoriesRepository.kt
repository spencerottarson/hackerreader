package com.ottarson.hackerreader.data.repositories

import com.ottarson.hackerreader.data.models.Story
import io.reactivex.Observable

interface StoriesRepository {
    fun getTopStories(
        startIndex: Int = 0,
        count: Int = 20,
        forceRefresh: Boolean = false
    ): Observable<Story>

    fun getStory(id: Int): Observable<Story>
}
