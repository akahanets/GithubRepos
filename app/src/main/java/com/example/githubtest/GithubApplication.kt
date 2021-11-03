package com.example.githubtest

import android.app.Activity
import android.app.Application
import android.content.Context
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


class GithubApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        // Dagger injection
        DaggerApplicationComponent
                .builder()
                .application(this)
                .build()
                .inject(this)
    }


    override fun androidInjector(): AndroidInjector<Any> =
            dispatchingAndroidInjector
}