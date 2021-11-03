package com.example.githubtest.data

import com.example.githubtest.domain.ReposContainer
import com.example.githubtest.domain.RepositoryContract
import io.reactivex.Single
import retrofit2.HttpException
import javax.inject.Inject

class TrendingReposRepository @Inject constructor(private val webservice: GithubWebservice,
                                                  private val schedulersProvider: ISchedulersProvider
)
    : RepositoryContract.ITrendingReposRepository {
    override fun loadTrendingRepos(page: Int): Single<ReposContainer> =
            webservice
                    .loadTrendingRepos(page)
                    .subscribeOn(schedulersProvider.io())
                    .onErrorReturn { err ->
                        if(err is HttpException && err.code() == 422)
                            return@onErrorReturn ReposContainer(0, emptyList())

                        throw err
                    }
}