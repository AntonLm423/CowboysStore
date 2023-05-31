package com.example.cowboysstore.presentation.ui.profile

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cowboysstore.R
import com.example.cowboysstore.data.local.prefs.Preferences
import com.example.cowboysstore.domain.usecases.GetAppVersionUseCase
import com.example.cowboysstore.utils.LoadException
import com.example.cowboysstore.domain.entities.Profile
import com.example.cowboysstore.domain.usecases.GetProfileUseCase
import com.example.cowboysstore.domain.usecases.GetUserPhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.PrimitiveIterator
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val getAppVersionUseCase: GetAppVersionUseCase,
    private val getUserPhotoUseCase: GetUserPhotoUseCase
) : ViewModel() {

    @Inject
    lateinit var preferences: Preferences

    private val _uiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun loadData() {
        _uiState.update {
            ProfileUiState.Loading
        }

        viewModelScope.launch {
            try {
                val appVersion = getAppVersionUseCase.getAppVersion()

                val profile = withContext(Dispatchers.IO) {
                    getProfileUseCase.getProfile()
                }

                val userPhoto = withContext(Dispatchers.IO) {
                    getUserPhotoUseCase.getUserPhoto(profile.avatarId)
                }

                _uiState.update {
                    ProfileUiState.Success(profile, appVersion, userPhoto)
                }
            } catch (e: LoadException) {
                _uiState.update {
                    ProfileUiState.Error(
                        e.errorResId ?: R.string.unknown_error,
                        e.detailedErrorResId ?: R.string.unknown_error_message
                    )
                }
            }
        }
    }

    fun clearAccessToken() {
        preferences.accessToken = ""
    }

    fun getProfile(): Profile? {
        return when (val state = uiState.value) {
            is ProfileUiState.Success -> state.profile
            else -> null
        }
    }

    sealed class ProfileUiState {

        object Loading : ProfileUiState()

        class Success(val profile: Profile, val appVersion: String, val userPhoto: Bitmap?) : ProfileUiState()

        class Error(val errorResID: Int, val messageResId: Int) : ProfileUiState()
    }
}

