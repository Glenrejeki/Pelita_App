package com.example.pelitaapp.core.data.remote

import com.example.pelitaapp.core.data.model.Profile
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Handle auth + memastikan profile row ada di table `profiles`.
 */
class AuthRemoteDataSource(
    private val client: SupabaseClient = SupabaseClientProvider.get()
) {

    /**
     * Register user (email/password) + buat row profiles (upsert).
     */
    suspend fun signUp(
        email: String,
        password: String,
        username: String,
        fullName: String? = null
    ): Profile {
        client.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }

        val userId = requireNotNull(client.auth.currentUserOrNull()?.id) {
            "Sign up berhasil tapi user null. Cek konfigurasi Auth Supabase."
        }

        // Pastikan row profiles ada
        val dto = ProfileRowDto(
            id = userId,
            username = username,
            fullName = fullName,
            bio = null,
            avatarUrl = null,
            createdAt = Instant.now().toString()
        )

        client.from("profiles").upsert(dto)

        return dto.toDomain()
    }

    /**
     * Login user (email/password).
     */
    suspend fun signIn(email: String, password: String) {
        client.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun signOut() {
        client.auth.signOut()
    }

    fun getCurrentUserId(): String? = client.auth.currentUserOrNull()?.id

    /**
     * Ambil profile current user dari table `profiles`.
     */
    suspend fun getMyProfile(): Profile? {
        val userId = getCurrentUserId() ?: return null

        val result = client.from("profiles")
            .select {
                filter { eq("id", userId) }
                limit(1)
            }
            .decodeList<ProfileRowDto>()

        return result.firstOrNull()?.toDomain()
    }

    /**
     * Update bio dan fullName (opsional).
     */
    suspend fun updateMyProfile(
        username: String? = null,
        fullName: String? = null,
        bio: String? = null,
        avatarUrl: String? = null
    ) {
        val userId = requireNotNull(getCurrentUserId()) { "User belum login." }

        val update = mutableMapOf<String, Any?>(
            "id" to userId
        )
        if (username != null) update["username"] = username
        if (fullName != null) update["full_name"] = fullName
        if (bio != null) update["bio"] = bio
        if (avatarUrl != null) update["avatar_url"] = avatarUrl

        client.from("profiles").update(update) {
            filter { eq("id", userId) }
        }
    }

    // ============================
    // DTO (Supabase table mapping)
    // ============================

    @Serializable
    data class ProfileRowDto(
        val id: String,
        val username: String,
        @SerialName("full_name") val fullName: String? = null,
        val bio: String? = null,
        @SerialName("avatar_url") val avatarUrl: String? = null,
        @SerialName("created_at") val createdAt: String? = null
    ) {
        fun toDomain(): Profile {
            return Profile(
                id = id,
                username = username,
                fullName = fullName,
                bio = bio,
                avatarUrl = avatarUrl,
                createdAt = createdAt?.let { Instant.parse(it) }
            )
        }
    }
}
