package com.example.pelitaapp.core.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

/**
 * Singleton provider untuk SupabaseClient
 * Panggil init(url, anonKey) sekali (biasanya di DI / Application)
 */
object SupabaseClientProvider {

    @Volatile
    private var client: SupabaseClient? = null

    fun init(
        supabaseUrl: String,
        supabaseAnonKey: String
    ) {
        if (client != null) return

        synchronized(this) {
            if (client == null) {
                client = createSupabaseClient(
                    supabaseUrl = supabaseUrl,
                    supabaseKey = supabaseAnonKey
                ) {
                    install(Auth)
                    install(Postgrest)
                    install(Storage)
                }
            }
        }
    }

    fun get(): SupabaseClient {
        return client ?: error("SupabaseClientProvider belum di-init. Panggil SupabaseClientProvider.init(...) dulu.")
    }
}
