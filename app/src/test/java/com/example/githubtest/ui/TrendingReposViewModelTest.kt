package com.example.githubtest.ui

import com.example.githubtest.data.GithubWebservice
import com.example.githubtest.data.ISchedulersProvider
import com.example.githubtest.domain.Repo
import com.example.githubtest.domain.ReposContainer
import com.example.githubtest.domain.RepositoryContract
import com.example.githubtest.ui.trendings.TrendingReposViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.*

import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.githubtest.data.Error
import okhttp3.ResponseBody.Companion.toResponseBody

import org.junit.Rule
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.RuntimeException
import java.net.SocketTimeoutException


class TrendingReposViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testSuccessLoadFewPages() {

        val repository = mockk<RepositoryContract.ITrendingReposRepository>()
        val schedulers = mockk<ISchedulersProvider>()

        every { schedulers.io() } returns Schedulers.trampoline()
        every { schedulers.ui() } returns Schedulers.trampoline()

        every { repository.loadTrendingRepos(1) } returns Single.just(ReposContainer(3, listOf(mockk(), mockk(), mockk())))
        every { repository.loadTrendingRepos(2) } returns Single.just(ReposContainer(3, listOf(mockk(), mockk(), mockk())))
        every { repository.loadTrendingRepos(3) } returns Single.just(ReposContainer(0, emptyList()))

        val vm = TrendingReposViewModel(repository, schedulers)

        assertNull(vm.errorsLiveData.value)
        assertNull(vm.reposLiveData.value)

        vm.loadTrendingRepos()

        assertEquals(Error.SUCCESS, vm.errorsLiveData.value)
        assertEquals(3, vm.reposLiveData.value!!.size)

        vm.loadTrendingRepos()

        assertEquals(Error.SUCCESS, vm.errorsLiveData.value)
        assertEquals(3, vm.reposLiveData.value!!.size)

        vm.loadTrendingRepos()

        assertEquals(Error.SUCCESS, vm.errorsLiveData.value)
        assert(vm.reposLiveData.value!!.isEmpty())

        verify(exactly = 1) { repository.loadTrendingRepos(1) }
        verify(exactly = 1) { repository.loadTrendingRepos(2) }
        verify(exactly = 1) { repository.loadTrendingRepos(3) }
    }

    @Test
    fun testSuccessLoadFewPagesAndReset() {

        val repository = mockk<RepositoryContract.ITrendingReposRepository>()
        val schedulers = mockk<ISchedulersProvider>()

        every { schedulers.io() } returns Schedulers.trampoline()
        every { schedulers.ui() } returns Schedulers.trampoline()

        every { repository.loadTrendingRepos(1) } returns Single.just(ReposContainer(3, listOf(mockk(), mockk(), mockk())))
        every { repository.loadTrendingRepos(2) } returns Single.just(ReposContainer(4, listOf(mockk(), mockk(), mockk(), mockk())))

        val vm = TrendingReposViewModel(repository, schedulers)

        assertNull(vm.errorsLiveData.value)
        assertNull(vm.reposLiveData.value)

        vm.loadTrendingRepos()

        assertEquals(Error.SUCCESS, vm.errorsLiveData.value)
        assertEquals(3, vm.reposLiveData.value!!.size)

        vm.loadTrendingRepos()

        assertEquals(Error.SUCCESS, vm.errorsLiveData.value)
        assertEquals(4, vm.reposLiveData.value!!.size)

        vm.loadTrendingRepos(reset = true)

        assertEquals(Error.SUCCESS, vm.errorsLiveData.value)
        assertEquals(3, vm.reposLiveData.value!!.size)

        vm.loadTrendingRepos()

        assertEquals(Error.SUCCESS, vm.errorsLiveData.value)
        assertEquals(4, vm.reposLiveData.value!!.size)

        verify(exactly = 2) { repository.loadTrendingRepos(1) }
        verify(exactly = 2) { repository.loadTrendingRepos(2) }
    }

    @Test
    fun testErrorsLoadPages() {

        val repository = mockk<RepositoryContract.ITrendingReposRepository>()
        val schedulers = mockk<ISchedulersProvider>()

        every { schedulers.io() } returns Schedulers.trampoline()
        every { schedulers.ui() } returns Schedulers.trampoline()

        every { repository.loadTrendingRepos(1) } returns
                Single.error(HttpException(Response.error<Any>(404, "error".toResponseBody()))) andThen
                Single.error(SocketTimeoutException()) andThen
                Single.error(IOException()) andThen
                Single.error(RuntimeException())

        val vm = TrendingReposViewModel(repository, schedulers)

        assertNull(vm.errorsLiveData.value)
        assertNull(vm.reposLiveData.value)

        vm.loadTrendingRepos()

        assertNull(vm.reposLiveData.value)
        assertEquals(Error.UNKNOWN, vm.errorsLiveData.value)

        vm.loadTrendingRepos()

        assertNull(vm.reposLiveData.value)
        assertEquals(Error.TIMEOUT, vm.errorsLiveData.value)

        vm.loadTrendingRepos()

        assertNull(vm.reposLiveData.value)
        assertEquals(Error.DISCONNECTED, vm.errorsLiveData.value)

        vm.loadTrendingRepos()

        assertNull(vm.reposLiveData.value)
        assertEquals(Error.UNKNOWN, vm.errorsLiveData.value)
    }
}