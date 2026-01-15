package com.android.catchdesign.data.services

import com.android.catchdesign.domain.models.ContentItem
import retrofit2.http.GET

interface ApiService {
    @GET("data/data.json")
    suspend fun getContent(): List<ContentItem>
}