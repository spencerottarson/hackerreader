package com.ottarson.hackerreader

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HackerReaderAppModule(private val application: HackerReaderApplication) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return application
    }
}
