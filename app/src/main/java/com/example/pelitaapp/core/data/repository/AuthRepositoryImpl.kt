package com.example.pelitaapp.core.data.repository

import com.example.pelitaapp.core.data.model.Profile
import com.example.pelitaapp.core.data.remote.AuthRemoteDataSource

class AuthRepositoryImpl(
    private val remote: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun signUp(
        email: String,
        password: String,
        username: String,
        fullName: String?
    ): Profile {
        return remote.signUp(
            email = email,
            password = password,
            username = username,
            fullName = fullName
        )
    }

    override suspend fun signIn(email: String, password: String) {
        remote.signIn(email, password)
    }

    override suspend fun signOut() {
        remote.signOut()
    }

    override fun getCurrentUserId(): String? = remote.getCurrentUserId()

    override suspend fun getMyProfile(): Profile? = remote.getMyProfile()

    override suspend fun updateMyProfile(
        username: String?,
        fullName: String?,
        bio: String?,
        avatarUrl: String?
    ) {
        remote.updateMyProfile(
            username = username,
            fullName = fullName,
            bio = bio,
            avatarUrl = avatarUrl
        )
    }
}
