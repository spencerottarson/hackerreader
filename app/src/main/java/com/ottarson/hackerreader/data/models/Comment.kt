package com.ottarson.hackerreader.data.models

data class Comment(
    val by: String?,
    val id: Int?,
    val kids: ArrayList<Int>?,
    val parent: Int?,
    val text: String?,
    val time: Long?,
    val type: String?
)
