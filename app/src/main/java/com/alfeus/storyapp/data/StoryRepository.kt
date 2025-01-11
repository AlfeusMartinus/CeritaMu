package com.alfeus.storyapp.data

import com.alfeus.storyapp.data.api.ApiService
import com.alfeus.storyapp.data.response.AddStoryResponse
import com.alfeus.storyapp.data.response.StoryDetailResponse
import com.alfeus.storyapp.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getAllStories(page: Int?, size: Int?, location: Int): StoryResponse {
        return apiService.getAllStories(page, size, location)
    }

    suspend fun getStoryDetail(id: String): StoryDetailResponse {
        return apiService.getStoryDetail(id)
    }

    suspend fun uploadStory(
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: Float? = null,
        lon: Float? = null
    ): AddStoryResponse {
        return apiService.uploadStory(photo, description, lat, lon)
    }

}
