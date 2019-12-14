package com.ottarson.hackerreader.data.network

import com.ottarson.hackerreader.data.models.Story
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface StoriesService {
    @GET("topstories.json")
    fun getStories(): Observable<List<Int>>

    @GET("item/{id}.json")
    fun getStory(@Path("id") id: Int): Observable<Story>
}
