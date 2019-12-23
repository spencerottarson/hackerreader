package com.ottarson.hackerreader.data.repositories

import com.ottarson.hackerreader.data.network.CommentsService
import com.ottarson.hackerreader.data.network.StoriesService
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideStoriesRepo(storiesService: StoriesService): StoriesRepository {
        return StoriesRepositoryImpl(storiesService)
    }

    @Provides
    fun provideCommentsRepo(commentsService: CommentsService): CommentsRepository {
        return CommentsRepositoryImpl(commentsService)
    }
}
