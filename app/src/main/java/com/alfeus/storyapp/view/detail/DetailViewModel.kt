package com.alfeus.storyapp.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfeus.storyapp.data.StoryRepository
import com.alfeus.storyapp.data.response.StoryDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: StoryRepository
) : ViewModel() {

    private val _storyDetail = MutableLiveData<StoryDetail>()
    val storyDetail: LiveData<StoryDetail> = _storyDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun fetchStoryDetail(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getStoryDetail(id)
                if (!response.error) {
                    _storyDetail.value = response.story
                } else {
                    _errorMessage.value = response.message
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

}