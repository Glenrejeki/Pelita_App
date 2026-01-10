package com.example.pelitaapp.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PelitaTopBar(
    title: String,
    onClickSearch: (() -> Unit)? = null,
    onClickSettings: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
        },
        colors = TopAppBarDefaults.topAppBarColors(),
        actions = {
            if (onClickSearch != null) {
                IconButton(onClick = onClickSearch) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
            if (onClickSettings != null) {
                IconButton(onClick = onClickSettings) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                }
            }
        }
    )
}
