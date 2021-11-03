package com.example.githubtest.ui.trendings

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubtest.data.Error
import com.example.githubtest.data.ISchedulersProvider
import com.example.githubtest.domain.Repo
import com.example.githubtest.domain.RepositoryContract
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException


class TrendingReposViewModel(
        private val repository: RepositoryContract.ITrendingReposRepository,
        private val schedulers: ISchedulersProvider
) : ViewModel() {
    private var page: Int = 1
    val errorsLiveData = MutableLiveData<Error>()
    val reposLiveData = MutableLiveData<List<Repo>>()

    @SuppressLint("CheckResult")
    fun loadTrendingRepos(reset: Boolean = false) {
        if (reset) {
            page = 1
        }
        repository
                .loadTrendingRepos(page)
                .observeOn(schedulers.ui())
                .subscribe({
                    errorsLiveData.value = Error.SUCCESS
                    reposLiveData.value = it.repos
                    page++
                }, { error ->
                    errorsLiveData.value = when (error) {
                        is HttpException -> Error.UNKNOWN
                        is SocketTimeoutException -> Error.TIMEOUT
                        is IOException -> Error.DISCONNECTED
                        else -> Error.UNKNOWN
                    }
                })
    }
}