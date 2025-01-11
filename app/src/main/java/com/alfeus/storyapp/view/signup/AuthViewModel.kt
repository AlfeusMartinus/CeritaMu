package com.alfeus.storyapp.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfeus.storyapp.data.AuthRepository
import com.alfeus.storyapp.data.response.RegisterResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
    val registerResult: LiveData<Result<RegisterResponse>> = _registerResult

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = authRepository.registerUser(name, email, password)
                _registerResult.postValue(Result.success(response))
            } catch (e: Exception) {
                _registerResult.postValue(Result.failure(e))
            }
        }
    }
}