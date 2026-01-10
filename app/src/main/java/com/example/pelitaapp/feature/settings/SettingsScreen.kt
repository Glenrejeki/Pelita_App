package com.example.pelitaapp.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pelitaapp.core.ui.components.Loading
import com.example.pelitaapp.core.ui.components.PelitaTopBar

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLoggedOut: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.errorMessage, state.successMessage) {
        // kalau mau pakai snackbar bisa
    }

    Scaffold(
        topBar = {
            PelitaTopBar(
                title = "Settings",
                onClickSearch = null,
                onClickSettings = null
            )
        }
    ) { innerPadding ->

        if (state.isLoading) {
            Loading(message = "Memuat settings...")
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = "Preferences", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(10.dp))

            // Theme toggle row
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                RowSwitch(
                    title = "Dark Mode",
                    subtitle = "Aktifkan tema gelap",
                    checked = state.isDarkMode,
                    onCheckedChange = viewModel::setDarkMode
                )
            }

            Spacer(Modifier.height(12.dp))
            Divider()
            Spacer(Modifier.height(12.dp))

            Text(text = "Account", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(10.dp))

            state.errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }
            state.successMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = { viewModel.logout(onLoggedOut) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }

            Spacer(Modifier.height(10.dp))

            OutlinedButton(
                onClick = { viewModel.deleteAccount() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Account (Coming Soon)")
            }

            Spacer(Modifier.height(18.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }
    }
}

@Composable
private fun RowSwitch(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleSmall)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
