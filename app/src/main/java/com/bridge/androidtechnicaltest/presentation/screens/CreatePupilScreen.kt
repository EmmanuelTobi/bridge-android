package com.bridge.androidtechnicaltest.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.bridge.androidtechnicaltest.data.model.Pupil
import com.bridge.androidtechnicaltest.presentation.viewmodel.PupilViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePupilScreen(
    onNavigateBack: () -> Unit,
    viewModel: PupilViewModel = koinViewModel()
) {

    var name by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var lng by remember { mutableStateOf("") }
    var lat by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Student") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = country,
                onValueChange = { country = it },
                label = { Text("Country") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = lng,
                onValueChange = { lng = it },
                label = { Text("Longtitude") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = lng,
                onValueChange = { lng = it },
                label = { Text("Longtitude") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = lat,
                onValueChange = { lat = it },
                label = { Text("Latitude") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val pupil = Pupil(
                        name = name,
                        image = "",
                        latitude = 0.0,
                        longitude = 0.0,
                        country = "",
                    )
                    viewModel.createStudent(pupil)
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = name.isNotBlank() && country.isNotBlank() &&
                         lat.isNotBlank() && lng.isNotBlank()
            ) {
                Text("Create Student")
            }
        }
    }
}