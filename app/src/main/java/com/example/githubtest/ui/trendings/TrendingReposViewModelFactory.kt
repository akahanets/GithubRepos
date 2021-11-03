package com.example.githubtest.ui.trendings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubtest.data.ISchedulersProvider
import com.example.githubtest.domain.RepositoryContract

class TrendingReposViewModelFactory constructor(
        private val repository: RepositoryContract.ITrendingReposRepository,
        private val schedulers: ISchedulersProvider
) :
        ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TrendingReposViewModel(repository, schedulers) as T
    }
}