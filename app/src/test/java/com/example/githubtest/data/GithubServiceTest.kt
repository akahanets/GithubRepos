
package com.example.githubtest.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(JUnit4::class)
class GithubServiceTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: GithubWebservice

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(GithubWebservice::class.java)

    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun testSuccessGithubWerservice() {
        enqueueResponse("repos.json")

        val result = service.loadTrendingRepos(1).blockingGet()
        val request = mockWebServer.takeRequest()

        assertEquals(123123, result.totalCount)
        assertTrue(result.repos.isNotEmpty())

        assertEquals("GET /search/repositories?sort=stars&order=desc&q=android&page=1 HTTP/1.1", request.requestLine)
    }

    @Test
    fun testErrorNoMorePagesGithubWerservice() {
        enqueueResponse("err_no_pages.json", responseCode = 422)

        val e = Assert.assertThrows(HttpException::class.java) {
            service.loadTrendingRepos(1).blockingGet()
        }

        assertEquals(422, e.code())
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap(), responseCode:Int = 200) {
        val inputStream = javaClass.classLoader.getResourceAsStream("api-response/$fileName")
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source.readString(Charsets.UTF_8))
                .setResponseCode(responseCode)
        )
    }
}
