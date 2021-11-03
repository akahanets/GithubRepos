package com.example.githubtest.ui.trendings

import androidx.lifecycle.ViewModelProvider
import com.example.githubtest.data.ISchedulersProvider
import com.example.githubtest.domain.RepositoryContract
import dagger.Module
import dagger.Provides

@Module
class TrendingReposModule {

    @Provides
    fun provideTrendingReposViewModelFactory(repository: RepositoryContract.ITrendingReposRepository, schedulers: ISchedulersProvider): ViewModelProvider.Factory =
            TrendingReposViewModelFactory(repository, schedulers)
}