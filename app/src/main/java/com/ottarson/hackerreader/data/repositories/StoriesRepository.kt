package com.ottarson.hackerreader.data.repositories

import com.ottarson.hackerreader.data.models.Story
import io.reactivex.rxjava3.core.Observable

interface StoriesRepository {
    fun getTopStories(
        startIndex: Int = 0,
        count: Int = 20,
        sort: String = "top",
        forceRefresh: Boolean = false
    ): Observable<Story>

    fun getStory(id: Int): Observable<Story>
}
