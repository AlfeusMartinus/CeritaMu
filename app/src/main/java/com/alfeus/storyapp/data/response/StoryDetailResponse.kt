package com.alfeus.storyapp.data.response

import com.google.gson.annotations.SerializedName

data class StoryDetailResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("story")
	val story: StoryDetail
)

data class StoryDetail(

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("lon")
	val lon: Double?,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("lat")
	val lat: Double?
)
