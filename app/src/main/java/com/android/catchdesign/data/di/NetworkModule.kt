package com.android.catchdesign.data.di

import com.android.catchdesign.data.repository.ContentRepositoryImpl
import com.android.catchdesign.data.services.ApiService
import com.android.catchdesign.domain.repository.ContentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
object NetworkModule {
    @Provides
    @ViewModelScoped
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/catchnz/ios-test/master/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideContentRepository(apiService: ApiService): ContentRepository {
        return ContentRepositoryImpl(apiService)
    }
}