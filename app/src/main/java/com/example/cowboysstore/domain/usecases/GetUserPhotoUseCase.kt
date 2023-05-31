package com.example.cowboysstore.domain.usecases

import android.graphics.Bitmap
import com.example.cowboysstore.domain.repository.RemoteRepository
import javax.inject.Inject

class GetUserPhotoUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    suspend fun getUserPhoto(id: String): Bitmap? {
        return remoteRepository.getUserPhoto(id).getOrElse {
            return null
        }
    }
}