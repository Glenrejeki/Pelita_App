package com.example.pelitaapp.core.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

// Kalau file screen kamu sudah ada, import yang bener.
// Kalau belum ada, NavGraph tetap aman karena ada fallback placeholder di bawah.
import com.example.pelitaapp.feature.feed.FeedScreen
import com.example.pelitaapp.feature.post.CreatePostScreen
import com.example.pelitaapp.feature.post.PostDetailScreen
import com.example.pelitaapp.feature.profile.ProfileScreen
import com.example.pelitaapp.feature.search.SearchScreen
import com.example.pelitaapp.feature.settings.SettingsScreen

@Composable
fun PelitaNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.FEED,
        modifier = modifier
    ) {
        // Bottom tabs
        composable(Routes.FEED) {
            // Pastikan FeedScreen kamu punya parameter yang sesuai.
            // Ini versi umum: onClickPost + onClickCreatePost
            runCatching {
                FeedScreen(
                    onOpenPost = { postId ->
                        navController.navigate(Routes.postDetail(postId))
                    },
                    onCreatePost = {
                        navController.navigate(Routes.CREATE_POST)
                    }
                )
            }.getOrElse {
                PlaceholderScreen("FeedScreen belum dibuat / signature beda")
            }
        }

        composable(Routes.SEARCH) {
            runCatching {
                SearchScreen(
                    onOpenPost = { postId ->
                        navController.navigate(Routes.postDetail(postId))
                    },
                    onOpenProfile = { userId ->
                        // untuk sekarang belum ada route profile detail
                        // nanti bisa dibuat: profile/{userId}
                    }
                )
            }.getOrElse {
                PlaceholderScreen("SearchScreen belum dibuat / signature beda")
            }
        }

        composable(Routes.PROFILE) {
            runCatching {
                ProfileScreen(
                    onOpenSettings = {
                        navController.navigate(Routes.SETTINGS)
                    }
                )
            }.getOrElse {
                PlaceholderScreen("ProfileScreen belum dibuat / signature beda")
            }
        }

        composable(Routes.SETTINGS) {
            runCatching {
                SettingsScreen(
                    onBack = { navController.popBackStack() }
                )
            }.getOrElse {
                PlaceholderScreen("SettingsScreen belum dibuat / signature beda")
            }
        }

        // Extra screens
        composable(Routes.CREATE_POST) {
            runCatching {
                CreatePostScreen(
                    onBack = { navController.popBackStack() },
                    onPostCreated = { newPostId ->
                        // setelah post dibuat, balik ke feed atau buka detail
                        navController.navigate(Routes.postDetail(newPostId)) {
                            popUpTo(Routes.FEED) { inclusive = false }
                        }
                    }
                )
            }.getOrElse {
                PlaceholderScreen("CreatePostScreen belum dibuat / signature beda")
            }
        }

        composable(
            route = Routes.POST_DETAIL,
            arguments = listOf(
                navArgument("postId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: ""

            runCatching {
                PostDetailScreen(
                    postId = postId,
                    onBack = { navController.popBackStack() }
                )
            }.getOrElse {
                PlaceholderScreen("PostDetailScreen belum dibuat / signature beda\npostId=$postId")
            }
        }
    }
}

/**
 * Placeholder supaya project tetap compile walau screen belum dibuat / param beda.
 */
@Composable
private fun PlaceholderScreen(text: String) {
    Surface {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = text, style = MaterialTheme.typography.titleMedium)
        }
    }
}
