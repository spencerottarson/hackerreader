package com.ottarson.hackerreader.ui.comments

import com.ottarson.hackerreader.data.models.Comment
import com.ottarson.hackerreader.utils.addTime
import java.util.Calendar
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CommentViewObjectTest {

    lateinit var comment: CommentViewObject

    @Before
    fun setup() {
        comment = CommentViewObject(commentApiModel, 1)
    }

    @Test
    fun getId() {
        assertEquals(50, comment.id)
    }

    @Test
    fun getAuthor() {
        assertEquals("author", comment.author)
    }

    @Test
    fun getText() {
        assertEquals("comment &", comment.text.toString())
    }

    @Test
    fun getTime() {
        assertEquals(commentTime.time / 1000, comment.time!!.time / 1000)
    }

    @Test
    fun getHeading() {
        assertEquals("author • 2 hours ago", comment.heading)
    }

    @Test
    fun getChildIds() {
        assertEquals(arrayListOf(50, 51, 52), comment.childIds)
    }

    @Test
    fun getCollapsed() {
        assertFalse(comment.collapsed)
    }

    @Test
    fun getHidden() {
        assertFalse(comment.hidden)
    }

    @Test
    fun getDepth() {
        assertEquals(1, comment.depth)
    }

    private val commentTime = Date().addTime(-2, Calendar.HOUR)

    private var commentApiModel = Comment(
        "author",
        50,
        arrayListOf(50, 51, 52),
        1,
        "comment &amp;  ",
        commentTime.time / 1000,
        "comment"
    )
}
