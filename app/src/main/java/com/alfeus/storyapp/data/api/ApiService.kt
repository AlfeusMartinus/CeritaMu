package com.alfeus.storyapp.data.api

import com.alfeus.storyapp.data.response.AddStoryResponse
import com.alfeus.storyapp.data.response.LoginRequest
import com.alfeus.storyapp.data.response.LoginResponse
import com.alfeus.storyapp.data.response.RegisterRequest
import com.alfeus.storyapp.data.response.RegisterResponse
import com.alfeus.storyapp.data.response.StoryDetailResponse
import com.alfeus.storyapp.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): RegisterResponse

    @POST("/v1/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @GET("stories")
    suspend fun getAllStories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = 0
    ): StoryResponse

    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Path("id") id: String
    ): StoryDetailResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float? = null,
        @Part("lon") lon: Float? = null
    ): AddStoryResponse

}