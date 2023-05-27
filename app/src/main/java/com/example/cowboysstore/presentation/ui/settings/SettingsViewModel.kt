package com.example.cowboysstore.presentation.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cowboysstore.data.model.Profile
import com.example.cowboysstore.data.model.ProfileChanges
import com.example.cowboysstore.domain.usecases.ChangeProfileUseCase
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
class SettingsViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val changeProfileUseCase: ChangeProfileUseCase
) : ViewModel() {

    companion object {
        const val pathName = "/name"
        const val pathSurname = "/surname"
        const val pathOccupation = "/occupation"
    }

    private val _uiState: MutableStateFlow<SettingsUiState> = MutableStateFlow(SettingsUiState.Empty)
    val uiState: StateFlow<SettingsUiState> = _uiState

    private val _profileEditResult: MutableLiveData<Boolean> = MutableLiveData()
    val profileEditResult: LiveData<Boolean> = _profileEditResult

    fun loadProfile() {
        viewModelScope.launch {
            try {
                val profile = withContext(Dispatchers.IO) {
                    getProfileUseCase.getProfile()
                }

                _uiState.update {
                    SettingsUiState.Success(profile)
                }
            } catch (e: Exception) {
                _uiState.update {
                    SettingsUiState.Empty
                }
            }
        }
    }

    fun changeProfile(oldProfile: Profile, newProfile: Profile) {
        val changedFields = mutableListOf<ProfileChanges>()

        if (oldProfile.name != newProfile.name) {
            changedFields.add(ProfileChanges(pathName, "replace", newProfile.name))
        }

        if (oldProfile.surname != newProfile.surname) {
            changedFields.add(ProfileChanges(pathSurname, "replace", newProfile.surname))
        }

        if (oldProfile.occupation != newProfile.occupation) {
            changedFields.add(ProfileChanges(pathOccupation, "replace", newProfile.occupation))
        }

        /*if (oldProfile.avatarId != newProfile.avatarId) {
           changes.add(ProfileChanges("avatarId", newProfile.avatarId))
       }*/
        viewModelScope.launch {
            try {
                changeProfileUseCase.changeProfile(changedFields)
                _profileEditResult.postValue(true)
            }
            catch (e : Exception) {
                _profileEditResult.postValue(false)
            }
        }
    }

    sealed class SettingsUiState {
        object Empty : SettingsUiState()
        data class Success(
            var profile: Profile
        ) : SettingsUiState()
    }
}