package com.example.pelitaapp.feature.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pelitaapp.core.ui.components.Loading
import com.example.pelitaapp.core.ui.components.PelitaTopBar

@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            PelitaTopBar(
                title = "Edit Profile",
                onClickSearch = null,
                onClickSettings = null
            )
        }
    ) { innerPadding ->

        if (state.isSaving) {
            Loading(message = "Menyimpan profil...")
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Ubah data profil kamu",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = state.fullNameInput,
                onValueChange = viewModel::onFullNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nama Lengkap") },
                singleLine = true
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = state.usernameInput,
                onValueChange = viewModel::onUsernameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Username") },
                singleLine = true
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = state.bioInput,
                onValueChange = viewModel::onBioChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Bio (max 160)") }
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = state.avatarUrlInput,
                onValueChange = viewModel::onAvatarUrlChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Avatar URL (opsional)") },
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            state.errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            state.successMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = { viewModel.saveProfile() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan")
            }

            Spacer(Modifier.height(8.dp))

            TextButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kembali")
            }
        }
    }
}
