package com.example.githubtest.data

import com.example.githubtest.API_REST_URL
import com.example.githubtest.NAMED_REST_API_URL
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    @Named(NAMED_REST_API_URL)
    fun provideRestApiUrl(): String = API_REST_URL

    @Provides
    fun provideSchedulersProvider(): ISchedulersProvider =
            SchedulersProvider()

}