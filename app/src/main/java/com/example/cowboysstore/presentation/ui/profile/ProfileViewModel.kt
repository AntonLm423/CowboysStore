package com.example.cowboysstore.presentation.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cowboysstore.R
import com.example.cowboysstore.domain.usecases.GetAppVersionUseCase
import com.example.cowboysstore.utils.LoadException
import com.example.cowboysstore.data.model.Profile
import com.example.cowboysstore.domain.usecases.GetProfileUseCase
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
    private val getProfileUseCase: GetProfileUseCase,
    private val getAppVersionUseCase: GetAppVersionUseCase
) : ViewModel() {

    private val _uiState : MutableStateFlow<ProfileUiState> = MutableStateFlow(ProfileUiState.Loading)
    val uiState : StateFlow<ProfileUiState> = _uiState

    fun loadData(accessToken : String) {
        _uiState.update {
            ProfileUiState.Loading
        }

        viewModelScope.launch {
            try {
                val appVersion = getAppVersionUseCase.getAppVersion()

                val profile = withContext(Dispatchers.IO) {
                    getProfileUseCase.getProfileByToken(accessToken)
                }

                _uiState.update {
                    ProfileUiState.Success(profile, appVersion)
                }
            }
            catch (e : LoadException) {
                _uiState.update {
                    ProfileUiState.Error(
                        e.errorResId ?: R.string.unknown_error,
                        e.messageResId ?: R.string.unknown_error_message
                    )
                }
            }
        }
    }

    sealed class ProfileUiState {

        object Loading : ProfileUiState()
        class Success(
            val profile : Profile,
            val appVersion : String
            ) : ProfileUiState()
        class Error(
            val errorResID : Int,
            val messageResId : Int
        ) : ProfileUiState()
    }

}

