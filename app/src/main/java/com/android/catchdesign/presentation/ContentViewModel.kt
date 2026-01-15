package com.android.catchdesign.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.catchdesign.domain.models.ContentItem
import com.android.catchdesign.domain.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor(
    private val repository: ContentRepository): ViewModel()
{
    private val _contents = MutableStateFlow<List<ContentItem>>(emptyList())
    val contents: StateFlow<List<ContentItem>> = _contents
    
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        fetchContent()
    }

    fun refreshContent() {
        fetchContent()
    }

    private fun fetchContent(){
        viewModelScope.launch {
            try {
                _isRefreshing.value = true
                _contents.value = repository.fetchContent()
            }catch (e:Exception){
                e.printStackTrace()
                Log.e("DesignUI","Error fetching content ${e.message}")
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}

