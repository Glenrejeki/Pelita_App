package com.example.pelitaapp.feature.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pelitaapp.core.ui.components.Loading
import com.example.pelitaapp.core.ui.components.PelitaTopBar
import com.example.pelitaapp.core.ui.components.PostCard

@Composable
fun SearchScreen(
    onOpenPost: (postId: String) -> Unit,
    onOpenProfile: (userId: String) -> Unit = {}, // placeholder
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.errorMessage) {
        // kalau mau pakai snackbar bisa ditambah nanti
    }

    Scaffold(
        topBar = {
            PelitaTopBar(
                title = "Search",
                onClickSearch = null,
                onClickSettings = null
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                value = state.query,
                onValueChange = viewModel::onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Cari firman / username...") },
                singleLine = true
            )

            Spacer(Modifier.height(10.dp))

            if (state.isLoading) {
                Loading(message = "Mencari...")
                return@Scaffold
            }

            state.errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            if (state.query.isBlank()) {
                Text(
                    text = "Ketik kata kunci untuk mulai mencari.",
                    style = MaterialTheme.typography.bodyMedium
                )
                return@Scaffold
            }

            Text(
                text = "Hasil Post (${state.postResults.size})",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (state.postResults.isEmpty()) {
                    item {
                        Text(
                            text = "Tidak ada hasil.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                items(state.postResults, key = { it.id }) { post ->
                    PostCard(
                        post = post,
                        onClickPost = { onOpenPost(it.id) },
                        onClickLike = null,
                        onClickComment = { onOpenPost(it.id) },
                        onClickRepost = null
                    )
                }
            }
        }
    }
}