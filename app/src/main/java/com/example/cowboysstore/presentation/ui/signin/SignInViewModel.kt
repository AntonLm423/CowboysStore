package com.example.cowboysstore.presentation.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cowboysstore.data.local.prefs.Preferences
import com.example.cowboysstore.domain.usecases.SignInUseCase
import com.example.cowboysstore.utils.LoadException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    @Inject
    lateinit var preferences: Preferences

    private val _authorizationState = MutableLiveData<AuthorizationState>()
    val authorizationState: LiveData<AuthorizationState> = _authorizationState

    fun authorize(email: String, password: String) {
        _authorizationState.value = AuthorizationState.Loading

        viewModelScope.launch {
            try {
                val result = signInUseCase.signIn(email, password)
                _authorizationState.value = AuthorizationState.Success(result)
            } catch (e: LoadException) {
                _authorizationState.value = AuthorizationState.Error(e.errorMessage ?: "")
            }
        }
    }

    fun saveAccessToken(accessToken: String) {
        preferences.accessToken = accessToken
    }

    fun checkAuthToken(): Boolean =
        preferences.accessToken.isNotEmpty()

    sealed class AuthorizationState {
        object Loading : AuthorizationState()
        data class Success(val accessToken: String) : AuthorizationState()
        data class Error(val errorMessage: String) : AuthorizationState()
    }
}