package com.ottarson.hackerreader.data.repositories

import com.ottarson.hackerreader.data.models.Comment
import io.reactivex.rxjava3.core.Observable

interface CommentsRepository {
    fun getComments(ids: ArrayList<Int>): Observable<Comment>
}
