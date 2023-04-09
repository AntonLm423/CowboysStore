package com.example.cowboysstore.presentation.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cowboysstore.domain.usecases.AuthorizationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authorizationUseCase: AuthorizationUseCase
) : ViewModel() {

    private val _state = MutableLiveData<AuthorizationState>()
    val state: LiveData<AuthorizationState> = _state

    fun authorize(email: String, password: String) {

        _state.value = AuthorizationState.Loading

        viewModelScope.launch {
            try {
                val result = authorizationUseCase.authorizationUseCase(email, password)
                _state.value = AuthorizationState.Success(result)
            } catch (e: Exception) {
                _state.value = AuthorizationState.Error(e)
            }
        }
    }

    sealed class AuthorizationState {
        object Loading : AuthorizationState()
        data class Success(val isAuthorized: Boolean) : AuthorizationState()
        data class Error(val error: Throwable) : AuthorizationState()
    }
}