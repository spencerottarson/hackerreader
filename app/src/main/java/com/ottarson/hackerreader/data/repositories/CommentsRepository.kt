package com.ottarson.hackerreader.data.repositories

import com.ottarson.hackerreader.data.models.Comment
import io.reactivex.Observable

interface CommentsRepository {
    fun getComments(ids: ArrayList<Int>): Observable<Comment>
}
