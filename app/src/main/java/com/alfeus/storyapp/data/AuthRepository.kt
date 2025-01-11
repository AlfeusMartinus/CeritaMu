package com.alfeus.storyapp.data

import com.alfeus.storyapp.data.api.ApiService
import com.alfeus.storyapp.data.pref.UserModel
import com.alfeus.storyapp.data.pref.UserPreference
import com.alfeus.storyapp.data.response.LoginRequest
import com.alfeus.storyapp.data.response.LoginResponse
import com.alfeus.storyapp.data.response.RegisterRequest
import com.alfeus.storyapp.data.response.RegisterResponse
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiService: ApiService, private val userPreference: UserPreference) {

    suspend fun registerUser(name: String, email: String, password: String): RegisterResponse {
        val request = RegisterRequest(name, email, password)
        try {
            return apiService.register(request)
        } catch (e: HttpException) {
            if (e.code() == 400) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = parseErrorMessage(errorBody)
                throw Exception(errorMessage)
            } else {
                throw e
            }
        }
    }

    private fun parseErrorMessage(errorBody: String?): String {
        return try {
            val jsonObject = JSONObject(errorBody ?: "")
            jsonObject.getString("message")
        } catch (e: Exception) {
            "Unknown error occurred"
        }
    }

    suspend fun loginUser(email: String, password: String): LoginResponse {
        val request = LoginRequest(email, password)
        return apiService.login(request)
    }

    suspend fun saveSession(email: String, token: String) {
        val user = UserModel(email = email, token = token, isLogin = true)
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }
}