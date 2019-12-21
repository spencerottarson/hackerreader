package com.ottarson.hackerreader.ui.shared;

import androidx.lifecycle.ViewModel;

import com.ottarson.hackerreader.data.repositories.CommentsRepository;
import com.ottarson.hackerreader.data.repositories.StoriesRepository;
import com.ottarson.hackerreader.ui.comments.CommentsViewModel;
import com.ottarson.hackerreader.ui.newslist.NewsListViewModel;

import dagger.MapKey;
import dagger.Module;
import dagger.multibindings.IntoMap;
import dagger.Provides;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;
import javax.inject.Provider;

@Module
public class ViewModelModule {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @MapKey
    @interface ViewModelKey {
        Class<? extends androidx.lifecycle.ViewModel> value();
    }

    @Provides
    ViewModelFactory viewModelFactory(
            Map<Class<? extends ViewModel>,
            Provider<ViewModel>> providerMap
    ) {
        return new ViewModelFactory(providerMap);
    }

    @Provides
    @IntoMap
    @ViewModelKey(NewsListViewModel.class)
    ViewModel newsListViewModel(StoriesRepository storiesRepository) {
        return new NewsListViewModel(storiesRepository);
    }

    @Provides
    @IntoMap
    @ViewModelKey(CommentsViewModel.class)
    ViewModel commentsViewModel(
            StoriesRepository storiesRepository,
            CommentsRepository commentsRepository
    ) {
        return new CommentsViewModel(storiesRepository, commentsRepository);
    }
}
