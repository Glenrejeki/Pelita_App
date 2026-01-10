package com.example.pelitaapp.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Wrapper DataStore untuk settings app.
 * Simpan preferensi seperti theme, onboarding, dsb.
 */

private const val DATASTORE_NAME = "pelita_settings"

// extension property untuk Context
private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

class SettingsDataStore(
    private val appContext: Context
) {

    // ========== THEME ==========
    val darkThemeEnabled: Flow<Boolean> =
        appContext.dataStore.data.map { prefs ->
            prefs[SettingsKeys.DARK_THEME_ENABLED] ?: false
        }

    suspend fun setDarkThemeEnabled(enabled: Boolean) {
        appContext.dataStore.edit { prefs ->
            prefs[SettingsKeys.DARK_THEME_ENABLED] = enabled
        }
    }

    // ========== OPTIONAL ==========
    val onboardingDone: Flow<Boolean> =
        appContext.dataStore.data.map { prefs ->
            prefs[SettingsKeys.ONBOARDING_DONE] ?: false
        }

    suspend fun setOnboardingDone(done: Boolean) {
        appContext.dataStore.edit { prefs ->
            prefs[SettingsKeys.ONBOARDING_DONE] = done
        }
    }

    val lastSyncEpoch: Flow<Long> =
        appContext.dataStore.data.map { prefs ->
            prefs[SettingsKeys.LAST_SYNC_EPOCH] ?: 0L
        }

    suspend fun setLastSyncEpoch(epochMillis: Long) {
        appContext.dataStore.edit { prefs ->
            prefs[SettingsKeys.LAST_SYNC_EPOCH] = epochMillis
        }
    }

    val locale: Flow<String?> =
        appContext.dataStore.data.map { prefs ->
            prefs[SettingsKeys.LOCALE]
        }

    suspend fun setLocale(locale: String?) {
        appContext.dataStore.edit { prefs ->
            if (locale == null) prefs.remove(SettingsKeys.LOCALE)
            else prefs[SettingsKeys.LOCALE] = locale
        }
    }

    // ========== UTIL ==========
    suspend fun clearAll() {
        appContext.dataStore.edit { it.clear() }
    }
}
