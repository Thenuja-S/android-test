package com.android.catchdesign.data.repository

import com.android.catchdesign.data.services.ApiService
import com.android.catchdesign.domain.models.ContentItem
import com.android.catchdesign.domain.repository.ContentRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class ContentRepositoryImplTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Mock
    private lateinit var mockApiService: ApiService

    private lateinit var repository: ContentRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        hiltRule.inject()
        repository = ContentRepositoryImpl(mockApiService)
    }

    @Test
    fun Test_Repository_Injected_ApiService() {
        assertNotNull("Repository should not be null", repository)
        assertTrue("Repository should be instance of ContentRepositoryImpl", 
            repository is ContentRepositoryImpl)
    }

    @Test
    fun Test_Fetch_getContent_Call_ApiService() = runTest {
        val mockContentItems = listOf(
            ContentItem(
                content = "Test content",
                id = 1,
                subtitle = "Test subtitle",
                title = "Test Title"
            )
        )
        whenever(mockApiService.getContent()).thenReturn(mockContentItems)
        val result = repository.fetchContent()
        verify(mockApiService, times(1)).getContent()
        assertEquals("Result should match mock data", mockContentItems, result)
        assertEquals("Result size should be 1", 1, result.size)
    }


    @Test
    fun Test_fetchContent_for_Multiple_Content() = runTest {
        val mockContentItems = listOf(
            ContentItem(content = "Content 1", id = 1, subtitle = "Subtitle 1", title = "Title 1"),
            ContentItem(content = "Content 2", id = 2, subtitle = "Subtitle 2", title = "Title 2"),
            ContentItem(content = "Content 3", id = 3, subtitle = "Subtitle 3", title = "Title 3")
        )
        whenever(mockApiService.getContent()).thenReturn(mockContentItems)
        val result = repository.fetchContent()
        verify(mockApiService, times(1)).getContent()
        assertEquals("Result size should be 3", 3, result.size)
        assertEquals("First item should match", "Title 1", result[0].title)
        assertEquals("Second item should match", "Title 2", result[1].title)
        assertEquals("Third item should match", "Title 3", result[2].title)
    }

    @Test(expected = Exception::class)
    fun Test_FetchContent_Throws_Failure_Exception() = runTest {
        whenever(mockApiService.getContent()).thenThrow(RuntimeException("API Error"))
        repository.fetchContent()
    }



    @Test
    fun ContentRepository_Interface_Implemented_ContentRepositoryImpl() {
        val condition:Boolean = repository is ContentRepository
        assertTrue("ContentRepositoryImpl should implement ContentRepository",
            condition)
        assertTrue("Repository should be ContentRepositoryImpl instance",
            repository is ContentRepositoryImpl)
    }

    @Test
    fun Test_APiService_Injection() = runTest {
        val testItems = listOf(ContentItem(content = "Test content", id = 1,
            subtitle = "Test subtitle",
            title = "Test"))
        whenever(mockApiService.getContent()).thenReturn(testItems)
        val result = repository.fetchContent()
        assertNotNull("ApiService should be injected and functional", mockApiService)
        assertEquals("Repository should use injected ApiService", testItems, result)
    }
}
