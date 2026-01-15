package com.android.catchdesign.domain.repository

import com.android.catchdesign.domain.models.ContentItem

interface ContentRepository {
    suspend fun fetchContent(): List<ContentItem>
}