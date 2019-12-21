package com.ottarson.hackerreader

import android.app.Application

class HackerReaderApplication : Application() {

    lateinit var component: HackerReaderComponent

    override fun onCreate() {
        super.onCreate()

        component = initializeComponent()
    }

    private fun initializeComponent(): HackerReaderComponent {
        return DaggerHackerReaderComponent.builder()
            .hackerReaderAppModule(HackerReaderAppModule(this))
            .build()
    }
}
