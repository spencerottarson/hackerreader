package com.ottarson.hackerreader

import com.ottarson.hackerreader.data.network.ApiModule
import com.ottarson.hackerreader.ui.comments.CommentsFragment
import com.ottarson.hackerreader.ui.newslist.NewsListFragment
import com.ottarson.hackerreader.ui.shared.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [HackerReaderAppModule::class, ViewModelModule::class, ApiModule::class])
interface HackerReaderComponent {
    fun inject(fragment: NewsListFragment)
    fun inject(fragment: CommentsFragment)
}
