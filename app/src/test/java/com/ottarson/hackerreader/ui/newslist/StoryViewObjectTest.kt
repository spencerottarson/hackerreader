package com.ottarson.hackerreader.ui.newslist

import com.ottarson.hackerreader.data.models.Story
import com.ottarson.hackerreader.utils.addTime
import java.util.Calendar
import java.util.Date
import kotlin.test.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class StoryViewObjectTest {

    private lateinit var story: StoryViewObject

    @Before
    fun setUp() {
        story = StoryViewObject(storyApiModel)
    }

    @Test
    fun getId() {
        assertEquals(1, story.id)
    }

    @Test
    fun getAuthor() {
        assertEquals("author", story.author)
    }

    @Test
    fun getNumComments() {
        assertEquals("12", story.numComments)
    }

    @Test
    fun getScore() {
        assertEquals(500, story.score)
    }

    @Test
    fun getTime() {
        assertEquals(storyTime.time / 1000, story.time!!.time / 1000)
    }

    @Test
    fun getTitle() {
        assertEquals("title1", story.title)
    }

    @Test
    fun getUrl() {
        assertEquals("https", story.url?.scheme)
        assertEquals("www.example.com", story.url?.host)
        assertEquals("/somepath", story.url?.path)
    }

    @Test
    fun getPoints() {
        assertEquals("500p", story.points)
    }

    @Test
    fun getSubtitle() {
        assertEquals("500p • 4 hours ago", story.subtitle)
    }

    @Test
    fun getDomain() {
        assertEquals("www.example.com", story.domain)
    }

    private val storyTime = Date().addTime(-4, Calendar.HOUR)

    private val storyApiModel = Story(
        "author",
        12,
        1,
        arrayListOf(10, 11, 12),
        500,
        storyTime.time / 1000,
        "title1",
        "story",
        "https://www.example.com/somepath"
    )
}
