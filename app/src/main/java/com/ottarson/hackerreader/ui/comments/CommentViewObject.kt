package com.ottarson.hackerreader.ui.comments

import android.text.Html
import com.ottarson.hackerreader.data.models.Comment
import com.ottarson.hackerreader.utils.getTimePast
import java.util.Date

enum class ViewState {
    collapsed,
    collapsing,
    expanding,
    expanded
}

class CommentViewObject(
    comment: Comment,
    val depth: Int = 0
) {
    val id = comment.id
    val author = comment.by
    val text = Html.fromHtml(comment.text ?: "").trim()
    val time = comment.time?.let { Date(it * 1000) }
    val heading = "$author • ${time?.getTimePast() ?: ""}"
    val childIds = comment.kids ?: arrayListOf()
    var viewState = ViewState.expanded
    var hidden = false
}
