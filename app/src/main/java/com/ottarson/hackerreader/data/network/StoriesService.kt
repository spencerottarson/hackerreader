package com.ottarson.hackerreader.data.network

import com.ottarson.hackerreader.data.models.Story
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface StoriesService {
    @GET("{sort}")
    fun getStories(@Path("sort") sort: String): Observable<List<Int>>

    @GET("item/{id}.json")
    fun getStory(@Path("id") id: Int): Observable<Story>
}
