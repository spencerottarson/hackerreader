package com.ottarson.hackerreader.data.repositories

import com.ottarson.hackerreader.data.models.Comment
import com.ottarson.hackerreader.data.network.CommentsService
import io.reactivex.Observable
import javax.inject.Inject

class CommentsRepository @Inject constructor(
    private val commentsService: CommentsService
) {

    fun getComments(ids: ArrayList<Int>): Observable<Comment> {
        if (ids.isNullOrEmpty()) {
            return Observable.empty()
        }

        return Observable.fromIterable(ids).flatMap { id: Int ->
            commentsService.getComment(id).flatMap { comment ->
                Observable.concat(
                    Observable.just(comment),
                    getComments(comment.kids ?: arrayListOf())
                )
            }
        }
    }
}
