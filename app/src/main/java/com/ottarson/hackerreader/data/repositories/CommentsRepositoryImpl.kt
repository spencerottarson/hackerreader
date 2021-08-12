package com.ottarson.hackerreader.data.repositories

import com.ottarson.hackerreader.data.models.Comment
import com.ottarson.hackerreader.data.network.CommentsService
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class CommentsRepositoryImpl @Inject constructor(
    private val commentsService: CommentsService
) : CommentsRepository {

    override fun getComments(ids: ArrayList<Int>): Observable<Comment> {
        if (ids.isNullOrEmpty()) {
            return Observable.empty()
        }

        return Observable.fromIterable(ids).concatMapEager { id: Int ->
            commentsService.getComment(id).concatMapEager { comment ->
                if (comment.deleted != true) {
                    Observable.concat(
                        Observable.just(comment),
                        getComments(comment.kids ?: arrayListOf())
                    )
                } else {
                    Observable.empty()
                }
            }
        }
    }
}
