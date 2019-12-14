package com.ottarson.hackerreader.data.network

import com.ottarson.hackerreader.data.models.Comment
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface CommentsService {
    @GET("item/{id}.json")
    fun getComment(@Path("id") id: Int): Observable<Comment>
}
