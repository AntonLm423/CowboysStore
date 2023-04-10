package com.example.cowboysstore.presentation.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cowboysstore.R
import com.example.cowboysstore.domain.usecases.GetAppVersionUseCase
import com.example.cowboysstore.presentation.utils.LoadException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getAppVersionUseCase: GetAppVersionUseCase
) : ViewModel() {

    private val _uiState : MutableStateFlow<ProfileUiState> = MutableStateFlow(ProfileUiState.Loading)
    val uiState : StateFlow<ProfileUiState> = _uiState

    init {
        getAppVersion()
    }

    fun getAppVersion() {
        _uiState.update {
            ProfileUiState.Loading
        }

        viewModelScope.launch {
           try {
                val appVersion = withContext(Dispatchers.IO) {
                    getAppVersionUseCase.getAppVersion()
                }

               _uiState.update {
                   ProfileUiState.Success(appVersion)
               }
           }
           catch (e : LoadException) {
               _uiState.update {
                   ProfileUiState.Error(e.errorResId ?: R.string.unknown_error)
               }
           }
        }

    }

    sealed class ProfileUiState {

        object Loading : ProfileUiState()
        class Success(val appVersion : String) : ProfileUiState()
        class Error(val errorResID : Int) : ProfileUiState()
    }

}

