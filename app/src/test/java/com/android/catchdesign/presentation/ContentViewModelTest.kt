package com.android.catchdesign.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.android.catchdesign.domain.models.ContentItem
import com.android.catchdesign.domain.repository.ContentRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
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
class ContentViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockRepository: ContentRepository

    private lateinit var viewModel: ContentViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun Test_ViewModel_Creation_with_Dependencies() {
        viewModel = ContentViewModel(mockRepository)
        assertNotNull("ViewModel should not be null", viewModel)
    }

    @Test
    fun Test_fetContent_Stateflow() = runTest {
        val mockContentItems = listOf(
            ContentItem(
                content = "Test content 1",
                id = 1,
                subtitle = "Test subtitle 1",
                title = "Test Title 1"
            ),
            ContentItem(
                content = "Test content 2",
                id = 2,
                subtitle = "Test subtitle 2",
                title = "Test Title 2"
            )
        )
        
        whenever(mockRepository.fetchContent()).thenReturn(mockContentItems)
        viewModel = ContentViewModel(mockRepository)
        advanceUntilIdle()

        // Then
        val contents = viewModel.contents.value
        assertEquals("Contents size should be 2", 2, contents.size)
        assertEquals("First item title should match", "Test Title 1", contents[0].title)
        assertEquals("Second item title should match", "Test Title 2", contents[1].title)
        verify(mockRepository, times(1)).fetchContent()
    }

    @Test
    fun Test_refreshing_Content_true_or_flase() = runTest {
        val mockContentItems = listOf(
            ContentItem(
                content = "Test content",
                id = 1,
                subtitle = "Test subtitle",
                title = "Test Title"
            )
        )
        
        whenever(mockRepository.fetchContent()).thenReturn(mockContentItems)
        viewModel = ContentViewModel(mockRepository)
        
        viewModel.isRefreshing.test {
            skipItems(1)
            advanceUntilIdle()
            assertEquals("isRefreshing should be true during fetch", true, awaitItem())
            assertEquals("isRefreshing should be false after fetch", false, awaitItem())
        }
    }

    @Test
    fun Test_refereshConten_Calls() = runTest {
        val mockContentItems = listOf(
            ContentItem(
                content = "Test content",
                id = 1,
                subtitle = "Test subtitle",
                title = "Test Title"
            )
        )
        
        whenever(mockRepository.fetchContent()).thenReturn(mockContentItems)
        viewModel = ContentViewModel(mockRepository)
        advanceUntilIdle()
        viewModel.refreshContent()
        advanceUntilIdle()
        verify(mockRepository, times(2)).fetchContent()
    }

    @Test
    fun Test_ContentViewMode() {
        viewModel = ContentViewModel(mockRepository)
        assertNotNull("ViewModel should be injected and not null", viewModel)
        assertNotNull("Contents StateFlow should be initialized", viewModel.contents)
        assertNotNull("IsRefreshing StateFlow should be initialized", viewModel.isRefreshing)
    }

    @Test
    fun Test_Injection_via_Constructor() = runTest {
        val mockContentItems = listOf(
            ContentItem(
                content = "Test content",
                id = 1,
                subtitle = "Test subtitle",
                title = "Test"
            )
        )
        whenever(mockRepository.fetchContent()).thenReturn(mockContentItems)
        viewModel = ContentViewModel(mockRepository)
        advanceUntilIdle()
        verify(mockRepository, atLeastOnce()).fetchContent()
        assertNotNull("Repository should be injected", mockRepository)
    }
}
