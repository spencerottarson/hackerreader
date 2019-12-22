package com.ottarson.hackerreader.ui.newslist

import android.net.Uri
import com.ottarson.hackerreader.data.models.Story
import com.ottarson.hackerreader.utils.getTimePast
import java.util.Date

class StoryViewObject(
    story: Story
) {
    val id = story.id
    val author = story.by
    val numComments = "${story.descendants ?: 0}"
    val score = story.score ?: 0
    val time = story.time?.let { Date(it * 1000) }
    val title = story.title
    val url = try { Uri.parse(story.url) } catch (e: Exception) { null }
    val points = "${score}p"
    val subtitle = "$points • ${time?.getTimePast() ?: ""}"
    val domain = url?.host
}
