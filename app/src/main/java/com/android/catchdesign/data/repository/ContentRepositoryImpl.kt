package com.android.catchdesign.data.repository

import com.android.catchdesign.data.services.ApiService
import com.android.catchdesign.domain.models.ContentItem
import com.android.catchdesign.domain.repository.ContentRepository

class ContentRepositoryImpl(private val apiService: ApiService): ContentRepository {
    override suspend fun fetchContent(): List<ContentItem> {
        return apiService.getContent()
    }
}