package com.ottarson.hackerreader.data.repositories

import com.ottarson.hackerreader.data.models.Story
import com.ottarson.hackerreader.data.network.StoriesService
import com.ottarson.hackerreader.utils.addTime
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.Observable
import java.util.Calendar
import java.util.Date
import org.junit.Before
import org.junit.Test

class StoriesRepositoryTest {

    lateinit var repository: StoriesRepository
    lateinit var service: StoriesService

    @Before
    fun setup() {
        service = mockk()

        every { service.getStories() } returns Observable.just(listOf(1, 2))
        every { service.getStory(1) } returns Observable.just(story)
        every { service.getStory(2) } returns Observable.just(story2)

        repository = StoriesRepositoryImpl(service)
    }

    @Test
    fun testGetTopStories_storiesReturned() {
        val testObservable = repository.getTopStories(0, 2).test()
        verify(exactly = 1) { service.getStories() }
        testObservable.assertValueAt(0) {
            it.title == "title1"
        }
        testObservable.assertValueAt(1) {
            it.title == "title2"
        }
        testObservable.assertComplete()
    }

    @Test
    fun testGetTopStories_fewerStoriesThanRequested() {
        val testObservable = repository.getTopStories().test()
        verify(exactly = 1) { service.getStories() }
        testObservable.assertValueCount(2)
        testObservable.assertComplete()
    }

    @Test
    fun testGetTopStories_startIndexOutOfRange() {
        val testObservable = repository.getTopStories(4).test()
        verify(exactly = 1) { service.getStories() }
        testObservable.assertNoValues()
        testObservable.assertComplete()
    }

    @Test
    fun testGetTopStories_idsCached() {
        var testObservable = repository.getTopStories(0, 2).test()
        verify(exactly = 1) { service.getStories() }
        testObservable.assertValueCount(2)
        testObservable = repository.getTopStories(0, 2).test()
        verify(exactly = 1) { service.getStories() }
        testObservable.assertValueCount(2)
    }

    @Test
    fun testGetTopStories_forceRefresh() {
        var testObservable = repository.getTopStories(0, 2).test()
        verify(exactly = 1) { service.getStories() }
        testObservable.assertValueCount(2)
        testObservable = repository.getTopStories(0, 2, true).test()
        verify(exactly = 2) { service.getStories() }
        testObservable.assertValueCount(2)
    }

    @Test
    fun getStory() {
        val testObservable = repository.getStory(1).test()
        testObservable.assertValue {
            it.title == "title1"
        }
        testObservable.assertComplete()
    }

    private val story = Story(
        "author",
        12,
        1,
        arrayListOf(10, 11, 12),
        500,
        Date().addTime(4, Calendar.HOUR).time / 1000,
        "title1",
        "story",
        "https://www.example.com/somepath"
    )

    private val story2 = Story(
        "author",
        12,
        2,
        arrayListOf(10, 11, 12),
        500,
        Date().addTime(4, Calendar.HOUR).time / 1000,
        "title2",
        "story",
        "https://www.example.com/somepath"
    )
}
