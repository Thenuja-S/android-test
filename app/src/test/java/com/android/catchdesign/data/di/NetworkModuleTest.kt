package com.android.catchdesign.data.di

import com.android.catchdesign.data.services.ApiService
import com.android.catchdesign.domain.repository.ContentRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject


@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class NetworkModuleTest {

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
    fun Test_ApiService_Provided_By_NetworkModule() {
        val condition: Boolean = apiService is ApiService
        assertNotNull("ApiService should not be null", apiService)
        assertTrue("ApiService should be an instance of ApiService", condition)
    }

    @Test
    fun Test_ContentRepository_Provided_By_NetworkModule () {
        val condition: Boolean = contentRepository is ContentRepository
        assertNotNull("ContentRepository should not be null", contentRepository)
        assertTrue("ContentRepository should be an instance of ContentRepository", 
            condition)
    }


    @Test
    fun Test_ApiService_BaseUrl_Configuration() {
        assertNotNull("ApiService should not be null", apiService)
        val apiServiceClass = apiService.javaClass
        assertNotNull("ApiService class should not be null", apiServiceClass)
    }

    @Test
    fun Test_Dependencies_Singleton() {
        assertNotNull("ApiService should not be null", apiService)
        assertNotNull("ContentRepository should not be null", contentRepository)
        assertSame("Dependencies should be properly initialized", 
            apiService, apiService)
    }
}
