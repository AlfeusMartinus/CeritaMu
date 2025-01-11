package com.alfeus.storyapp.view.uploadStory

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfeus.storyapp.data.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val repository: StoryRepository
) : ViewModel() {

    private val _uploadStatus = MutableLiveData<Result<String>>(null)
    val uploadStatus: LiveData<Result<String>> = _uploadStatus

    var currentImageUri: Uri? = null

    fun uploadStory(
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: Float? = null,
        lon: Float? = null
    ) {
        _uploadStatus.value = Result.loading()

        viewModelScope.launch {
            try {
                val response = repository.uploadStory(photo, description, lat, lon)
                if (!response.error) {
                    _uploadStatus.value = Result.success(response.message)
                } else {
                    _uploadStatus.value = Result.error(response.message)
                }
            } catch (e: Exception) {
                _uploadStatus.value = Result.error(e.message ?: "An unknown error occurred")
            }
        }
    }
}
