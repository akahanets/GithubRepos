package com.example.githubtest.data

import io.reactivex.Scheduler

interface ISchedulersProvider {
    fun ui(): Scheduler
    fun io(): Scheduler
}