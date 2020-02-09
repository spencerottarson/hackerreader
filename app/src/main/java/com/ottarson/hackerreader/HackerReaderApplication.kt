package com.ottarson.hackerreader

import android.app.Application

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
}
