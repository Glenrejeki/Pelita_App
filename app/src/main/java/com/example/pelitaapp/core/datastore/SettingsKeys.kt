package com.example.pelitaapp.core.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object SettingsKeys {
    // Theme
    val DARK_THEME_ENABLED = booleanPreferencesKey("dark_theme_enabled")

    // Optional (kalau nanti butuh)
    val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
    val LAST_SYNC_EPOCH = longPreferencesKey("last_sync_epoch")
    val LOCALE = stringPreferencesKey("locale")
}
