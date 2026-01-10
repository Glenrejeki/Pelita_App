package com.example.pelitaapp.core.data.model

import java.time.Instant

/**
 * Profile pengguna
 * Disinkronkan dengan tabel `profiles` di Supabase
 */
data class Profile(
    val id: String,
    val username: String,
    val fullName: String? = null,
    val bio: String? = null,
    val avatarUrl: String? = null,
    val createdAt: Instant? = null
)
