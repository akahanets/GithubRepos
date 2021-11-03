package com.example.githubtest.data

import com.example.githubtest.domain.ReposContainer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException
import retrofit2.Response
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class TrendingReposRepositoryTest {

    @Test
    fun testRepositorySchedulers() {

        val webservice = mockk<GithubWebservice>()
        val schedulers = mockk<ISchedulersProvider>()

        every { schedulers.io() } returns Schedulers.trampoline()
        every { schedulers.ui() } returns Schedulers.trampoline()

        every { webservice.loadTrendingRepos(1) } returns Single.just(ReposContainer(1, listOf(mockk())))

        val trendingReposRepository = TrendingReposRepository(webservice, schedulers)
        val res = trendingReposRepository.loadTrendingRepos(1).blockingGet()

        verify { schedulers.io() }
        assert(res.repos.isNotEmpty())
        assertEquals(1, res.totalCount)
    }

    @Test
    fun testRepositoryNoPagesToEmpty() {

        val webservice = mockk<GithubWebservice>()
        val schedulers = mockk<ISchedulersProvider>()

        every { schedulers.io() } returns Schedulers.trampoline()
        every { schedulers.ui() } returns Schedulers.trampoline()

        every { webservice.loadTrendingRepos(1) } returns Single.error(HttpException(Response.error<Any>(422, "error".toResponseBody())))

        val trendingReposRepository = TrendingReposRepository(webservice, schedulers)
        val res = trendingReposRepository.loadTrendingRepos(1).blockingGet()

        assertEquals(0, res.totalCount)
        assert(res.repos.isEmpty())
    }


}