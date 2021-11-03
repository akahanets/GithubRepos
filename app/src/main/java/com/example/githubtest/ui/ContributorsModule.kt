package com.example.githubtest.ui

import com.example.githubtest.data.TrendingReposRepositoryModule
import com.example.githubtest.ui.trendings.TrendingReposFragment
import com.example.githubtest.ui.trendings.TrendingReposModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ContributorsModule {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [TrendingReposRepositoryModule::class, TrendingReposModule::class])
    abstract fun bindTrendingReposFragment(): TrendingReposFragment
}