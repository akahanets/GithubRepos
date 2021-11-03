package com.example.githubtest.data

import com.example.githubtest.domain.ReposContainer
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubWebservice {

    @GET("search/repositories?sort=stars&order=desc&q=android")
    fun loadTrendingRepos(@Query("page") page: Int): Single<ReposContainer>
}