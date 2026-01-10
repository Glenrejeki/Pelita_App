package com.example.pelitaapp.core.ui.navigation

/**
 * Semua route aplikasi ditaruh di sini biar rapi dan konsisten.
 */
object Routes {
    // Bottom Tabs
    const val FEED = "feed"
    const val SEARCH = "search"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"

    // Extra screens
    const val CREATE_POST = "create_post"
    const val POST_DETAIL = "post_detail/{postId}"

    // Helpers
    fun postDetail(postId: String) = "post_detail/$postId"
}
