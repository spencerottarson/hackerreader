package com.ottarson.hackerreader.data.repositories

import com.ottarson.hackerreader.data.models.Comment
import com.ottarson.hackerreader.data.network.CommentsService
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

class CommentsRepository(
    private val commentsService: CommentsService
) {

    fun getComments(ids: ArrayList<Int>): Observable<Comment> {
        return Observable.create { emitter ->
            fetchComments(ids, emitter)
        }
    }

    private fun fetchComments(ids: ArrayList<Int>, emitter: ObservableEmitter<Comment>) {
        ids.forEach { id ->
            val comment = getComment(id)
            emitter.onNext(comment)
            comment.kids?.let { kids ->
                fetchComments(kids, emitter)
            }
        }
    }

    private fun getComment(id: Int): Comment {
        return commentsService.getComment(id).blockingSingle()
    }
}
