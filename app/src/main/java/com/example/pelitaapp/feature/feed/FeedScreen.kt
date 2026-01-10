package com.example.pelitaapp.feature.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pelitaapp.core.ui.components.Loading
import com.example.pelitaapp.core.ui.components.PelitaTopBar
import com.example.pelitaapp.core.ui.components.PostCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    onOpenPost: (postId: String) -> Unit,
    onCreatePost: () -> Unit,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    val snackHostState = remember { SnackbarHostState() }
    val pullState = rememberPullToRefreshState()

    // show snackbar kalau ada error
    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            snackHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            PelitaTopBar(
                title = "Pelita",
                onClickSearch = null,
                onClickSettings = null
            )
        },
        snackbarHost = { SnackbarHost(snackHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreatePost) {
                Icon(Icons.Default.Add, contentDescription = "Create Post")
            }
        }
    ) { innerPadding ->

        if (state.isLoading) {
            Loading(message = "Memuat feed...")
            return@Scaffold
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Pull to refresh
            if (pullState.isRefreshing) {
                LaunchedEffect(true) {
                    viewModel.refresh()
                    pullState.endRefresh()
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp)
            ) {
                if (state.posts.isEmpty()) {
                    item {
                        Text(
                            text = "Belum ada postingan.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                items(
                    items = state.posts,
                    key = { it.id }
                ) { post ->
                    PostCard(
                        post = post,
                        modifier = Modifier.padding(bottom = 10.dp),
                        onClickPost = { onOpenPost(it.id) },
                        onClickLike = { viewModel.onToggleLike(it) },
                        onClickComment = { onOpenPost(it.id) },
                        onClickRepost = { viewModel.onToggleRepost(it) }
                    )
                }
            }

            PullToRefreshContainer(
                state = pullState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}
