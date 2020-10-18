package com.ottarson.hackerreader

import com.ottarson.hackerreader.data.models.Comment
import com.ottarson.hackerreader.data.models.Story
import com.ottarson.hackerreader.data.network.CommentsService
import com.ottarson.hackerreader.data.network.StoriesService
import com.ottarson.hackerreader.data.repositories.RepositoryModule
import com.ottarson.hackerreader.ui.comments.CommentsFragment
import com.ottarson.hackerreader.ui.newslist.NewsListFragment
import com.ottarson.hackerreader.ui.shared.ViewModelModule
import com.ottarson.hackerreader.utils.addTime
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.Observable
import java.util.Calendar
import java.util.Date
import javax.inject.Singleton

@Singleton
@Component(modules = [
    HackerReaderAppModule::class,
    ViewModelModule::class,
    TestHackerReaderApplication.TestApiModule::class,
    RepositoryModule::class
])
interface TestHackerReaderComponent : HackerReaderComponent {
    override fun inject(fragment: NewsListFragment)
    override fun inject(fragment: CommentsFragment)
}

class TestHackerReaderApplication : HackerReaderApplication() {

    override fun initializeComponent(): HackerReaderComponent {
        return DaggerTestHackerReaderComponent.builder()
            .hackerReaderAppModule(HackerReaderAppModule(this))
            .build()
    }

    @Module
    class TestApiModule {
        @Provides
        fun provideCommentsService(): CommentsService {
            return object : CommentsService {
                override fun getComment(id: Int): Observable<Comment> {
                    return Observable.just(commentApiModel)
                }
            }
        }

        @Provides
        fun providesStoriesService(): StoriesService {
            return object : StoriesService {
                override fun getStories(): Observable<List<Int>> {
                    return Observable.just(listOf(1, 2))
                }

                override fun getStory(id: Int): Observable<Story> {
                    return when (id) {
                        1 -> Observable.just(story)
                        else -> Observable.just(story2)
                    }
                }
            }
        }

        private val commentTime = Date().addTime(-2, Calendar.HOUR)

        private var commentApiModel = Comment(
            "author",
            50,
            arrayListOf(),
            1,
            "comment &amp;  ",
            commentTime.time / 1000.toLong(),
            "comment"
        )

        private val story = Story(
            "author",
            12,
            1,
            arrayListOf(50),
            500,
            Date().addTime(-4, Calendar.HOUR).time / 1000,
            "title1",
            "story",
            "https://www.example.com/somepath"
        )

        private val story2 = Story(
            "author",
            12,
            2,
            arrayListOf(),
            300,
            Date().addTime(-6, Calendar.HOUR).time / 1000,
            "title2",
            "story",
            "https://www.example2.com/somepath2"
        )
    }
}
