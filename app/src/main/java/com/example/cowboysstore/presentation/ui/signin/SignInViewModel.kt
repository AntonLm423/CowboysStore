package com.example.cowboysstore.presentation.ui.signin

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cowboysstore.R
import com.example.cowboysstore.domain.usecases.AuthorizationUseCase
import com.example.cowboysstore.utils.LoadException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authorizationUseCase: AuthorizationUseCase
) : ViewModel() {

    private val _authorizationState = MutableLiveData<AuthorizationState>()
    val authorizationState: LiveData<AuthorizationState> = _authorizationState

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

        _authorizationState.value = AuthorizationState.Loading

        viewModelScope.launch {
            try {
                val result = authorizationUseCase.authorizationUseCase(email, password)
                _authorizationState.value = AuthorizationState.Success(result)
            }
            catch (e: LoadException) {
                _authorizationState.value = AuthorizationState.Error(e.errorResId ?: R.string.unknown_error)
            }
        }
    }

    sealed class AuthorizationState {
        object Loading : AuthorizationState()
        data class Success(val accessToken : String) : AuthorizationState()
        data class Error(val errorResId: Int) : AuthorizationState()
    }
}