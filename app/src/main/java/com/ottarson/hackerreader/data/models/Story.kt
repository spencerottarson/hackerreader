package com.ottarson.hackerreader.data.models

data class Story(
    val by: String?,
    val descendants: Int?,
    val id: Int?,
    val kids: ArrayList<Int>?,
    val score: Int?,
    val time: Long?,
    val title: String?,
    val type: String?,
    val url: String?
)
