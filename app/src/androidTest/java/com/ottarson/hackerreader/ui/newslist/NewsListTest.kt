package com.ottarson.hackerreader.ui.newslist

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.runner.AndroidJUnit4
import com.ottarson.hackerreader.R
import org.hamcrest.CoreMatchers.anything
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsListTest {

    @Test
    fun testNewsList() {
        val test = launchFragmentInContainer<NewsListFragment>()

        onView(withId(R.id.newsListView)).check(matches(hasChildCount(2)))

        onData(anything())
            .inAdapterView(withId(R.id.newsListView))
            .atPosition(0)
            .onChildView(withId(R.id.itemNewsListTitle))
            .check(matches(withText("title1")))

        onData(anything())
            .inAdapterView(withId(R.id.newsListView))
            .atPosition(0)
            .onChildView(withId(R.id.itemNewsListSubtitle))
            .check(matches(withText("500p • 4 hours ago")))

        onData(anything())
            .inAdapterView(withId(R.id.newsListView))
            .atPosition(0)
            .onChildView(withId(R.id.itemNewsListDomain))
            .check(matches(withText("www.example.com")))

        onData(anything())
            .inAdapterView(withId(R.id.newsListView))
            .atPosition(0)
            .onChildView(withId(R.id.itemNewsListNumComments))
            .check(matches(withText("12")))
    }
}
