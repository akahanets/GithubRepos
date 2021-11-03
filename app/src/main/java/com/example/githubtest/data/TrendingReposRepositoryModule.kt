package com.example.githubtest.data

import com.example.githubtest.domain.RepositoryContract
import dagger.Module
import dagger.Provides

@Module
class TrendingReposRepositoryModule {
    @Provides
    fun provideTrendingReposRepository(repository: TrendingReposRepository):
            RepositoryContract.ITrendingReposRepository = repository
}