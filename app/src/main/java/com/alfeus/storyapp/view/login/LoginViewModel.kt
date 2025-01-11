package com.alfeus.storyapp.view.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfeus.storyapp.data.AuthRepository
import com.alfeus.storyapp.data.pref.UserModel
import com.alfeus.storyapp.data.response.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = authRepository.loginUser(email, password)
                authRepository.saveSession(email, response.loginResult.token) // Simpan token
                _loginResult.postValue(Result.success(response))
            } catch (e: Exception) {
                _loginResult.postValue(Result.failure(e))
            }
        }
    }


    fun getSession(): LiveData<UserModel> {
        val userSession = MutableLiveData<UserModel>()
        viewModelScope.launch {
            authRepository.getSession().collect { session ->
                userSession.postValue(session)
            }
        }
        return userSession
    }

    fun saveToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("auth_token", token).apply()
        Log.d("LoginViewModel", "Token disimpan: $token")
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}