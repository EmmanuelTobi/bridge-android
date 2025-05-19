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
fun EditPupilScreen(
    studentId: Int,
    onNavigateBack: () -> Unit,
    viewModel: PupilViewModel = koinViewModel()
) {

    val pupil by viewModel.editPupil.collectAsState()

    var name by remember(pupil) { mutableStateOf(pupil?.name ?: "") }
    var country by remember(pupil) { mutableStateOf(pupil?.country ?: "") }
    var lng by remember(pupil) { mutableStateOf(pupil?.longitude?.toString() ?: "") }
    var lat by remember(pupil) { mutableStateOf(pupil?.latitude?.toString() ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Student") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                    label = { Text("Longitude") },
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

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        pupil?.let { currentPupil ->
                            val updatedStudent = currentPupil.copy(
                                name = name,
                                country = country,
                                longitude = lng.toDoubleOrNull() ?: 0.0,
                                latitude = lat.toDoubleOrNull() ?: 0.0
                            )
                            viewModel.updateStudent(studentId, updatedStudent)
                        }
                        onNavigateBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = name.isNotBlank() && country.isNotBlank() &&
                            lat.isNotBlank() && lng.isNotBlank()
                ) {
                    Text("Save Changes")
                }
            }
        }

    }
}