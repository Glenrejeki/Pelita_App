package com.example.pelitaapp.core.util

/**
 * Validator sederhana untuk Auth / input user.
 * Kamu bisa pakai ini di ViewModel sebelum panggil repository.
 */
object Validators {

    fun isEmailValid(email: String): Boolean {
        if (email.isBlank()) return false
        // simple email regex
        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return regex.matches(email.trim())
    }

    fun isPasswordValid(password: String): Boolean {
        // Minimal 8 karakter (kamu bisa ubah jadi 6 kalau mau)
        if (password.length < 8) return false
        return true
    }

    fun isUsernameValid(username: String): Boolean {
        val trimmed = username.trim()
        if (trimmed.length < 3) return false
        // hanya huruf, angka, underscore, titik
        val regex = "^[a-zA-Z0-9_.]+$".toRegex()
        return regex.matches(trimmed)
    }

    fun isPostContentValid(content: String): Boolean {
        // Biar postingan nggak kosong
        return content.trim().isNotEmpty()
    }

    fun isBioValid(bio: String): Boolean {
        // Batas aman (bisa kamu ubah)
        return bio.length <= 160
    }
}
