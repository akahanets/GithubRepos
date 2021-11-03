package com.example.githubtest

import com.example.githubtest.data.ApiServiceModule
import com.example.githubtest.data.ApiModule
import com.example.githubtest.ui.ApplicationModule
import com.example.githubtest.ui.ContributorsModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ApiServiceModule::class,
    ApiModule::class,
    ApplicationModule::class,
    ContributorsModule::class
])
interface ApplicationComponent : AndroidInjector<GithubApplication> {


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: GithubApplication): Builder

        fun build(): ApplicationComponent
    }

}