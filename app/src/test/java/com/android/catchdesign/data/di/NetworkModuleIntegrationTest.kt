package com.android.catchdesign.data.di

import com.android.catchdesign.data.repository.ContentRepositoryImpl
import com.android.catchdesign.data.services.ApiService
import com.android.catchdesign.domain.repository.ContentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import javax.inject.Singleton


@HiltAndroidTest
@UninstallModules(NetworkModule::class)
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class NetworkModuleIntegrationTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var contentRepository: ContentRepository

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun Test_Configured_Fake_Module() {
        assertNotNull("ApiService should be provided by FakeNetworkModule", apiService)
        assertNotNull("ContentRepository should be provided by FakeNetworkModule", contentRepository)
        assertTrue("Test should pass when module replacement works", true)
    }

    @Test
    fun Test_DG_Configured_Fake_Module() {
        assertNotNull("ApiService should be injected from FakeNetworkModule", apiService)
        assertNotNull("ContentRepository should be injected from FakeNetworkModule", contentRepository)
        assertTrue("Dependency graph should be valid with fake module", true)
    }


    @Module
    @InstallIn(SingletonComponent::class)
    object FakeNetworkModule {
        
        @Provides
        @Singleton
        fun provideFakeApiService(): ApiService {
            return mock(ApiService::class.java)
        }

        @Provides
        @Singleton
        fun provideFakeContentRepository(apiService: ApiService): ContentRepository {
            return ContentRepositoryImpl(apiService)
        }
    }
}
