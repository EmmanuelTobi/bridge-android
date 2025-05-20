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
import com.bridge.androidtechnicaltest.utils.ResultHandler
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePupilScreen(
    onNavigateBack: () -> Unit,
    viewModel: PupilViewModel = koinViewModel()
) {

    val creatingPupil by viewModel.createPupil.collectAsState()

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
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = lng,
                onValueChange = { lng = it },
                label = { Text("Longtitude") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = lat,
                onValueChange = { lat = it },
                label = { Text("Latitude") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = image,
                onValueChange = { image = it },
                label = { Text("Image") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val pupil = Pupil(
                        name = name,
                        image = image,
                        latitude = lat.toDoubleOrNull() ?: 0.0,
                        longitude = lng.toDoubleOrNull() ?: 0.0,
                        country = country,
                    )
                    viewModel.createPupil(pupil)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = name.isNotBlank() && country.isNotBlank() &&
                         lat.isNotBlank() && lng.isNotBlank() &&
                         creatingPupil !is ResultHandler.Loading
            ) {
                when(creatingPupil) {
                    is ResultHandler.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    is ResultHandler.Success -> {
                        Text("Success")
                        LaunchedEffect(Unit) {
                            onNavigateBack()
                        }
                    }
                    is ResultHandler.Error -> {
                        Text("Error")
                    }
                    null -> {
                        Text("Create Student")
                    }
                }
            }
        }
    }
}