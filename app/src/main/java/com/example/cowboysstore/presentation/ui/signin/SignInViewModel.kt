package com.example.cowboysstore.presentation.ui.signin

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cowboysstore.domain.usecases.AuthorizationUseCase
import com.example.cowboysstore.utils.AuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authorizationUseCase: AuthorizationUseCase
) : ViewModel() {

    private val _state = MutableLiveData<AuthorizationState>()
    val state: LiveData<AuthorizationState> = _state

    fun validate(email : String, password: String) : Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false
        }

        if (password.length < 8) {
            return false
        }

        return true
    }

    fun authorize(email: String, password: String) {

        _state.value = AuthorizationState.Loading

        viewModelScope.launch {
            try {
                val result = authorizationUseCase.authorizationUseCase(email, password)
                    Log.d("TEST_ABC", result)
                    _state.value = AuthorizationState.Success(result)
            }
            catch (e: AuthException) {
                _state.value = AuthorizationState.Error(e.errorMessage)

            }
        }
    }

    sealed class AuthorizationState {
        object Loading : AuthorizationState()
        data class Success(val accessToken : String) : AuthorizationState()
        data class Error(val errorMessage: String) : AuthorizationState()
    }
}