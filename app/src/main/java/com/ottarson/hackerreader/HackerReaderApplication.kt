package com.ottarson.hackerreader

import android.app.Application
import com.ottarson.hackerreader.data.models.Comment
import com.ottarson.hackerreader.utils.addTime
import java.util.Calendar
import java.util.Date

open class HackerReaderApplication : Application() {

    lateinit var component: HackerReaderComponent

    override fun onCreate() {
        super.onCreate()

        component = initializeComponent()
    }

    open fun initializeComponent(): HackerReaderComponent {
        return DaggerHackerReaderComponent.builder()
            .hackerReaderAppModule(HackerReaderAppModule(this))
            .build()
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
