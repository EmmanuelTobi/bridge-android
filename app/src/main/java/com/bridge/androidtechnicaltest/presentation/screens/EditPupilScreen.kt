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
fun EditPupilScreen(
    studentId: Int,
    pupil: Pupil?,
    onNavigateBack: () -> Unit,
    viewModel: PupilViewModel = koinViewModel()
) {

    val viewModelPupil by viewModel.editPupil.collectAsState()
    val updatePupilViewModel by viewModel.updatePupil.collectAsState()

    val pupilData = pupil ?: viewModelPupil

    LaunchedEffect(pupil) {
        if (pupil != null && viewModelPupil == null) {
            viewModel.setEditPupil(pupil)
        }
    }

    var name by remember(pupilData) { mutableStateOf(pupilData?.name ?: "") }
    var country by remember(pupilData) { mutableStateOf(pupilData?.country ?: "") }
    var lng by remember(pupilData) { mutableStateOf(pupilData?.longitude?.toString() ?: "") }
    var lat by remember(pupilData) { mutableStateOf(pupilData?.latitude?.toString() ?: "") }
    var image by remember(pupilData) { mutableStateOf(pupilData?.image ?: "") }

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

                OutlinedTextField(
                    value = image,
                    onValueChange = { image = it },
                    label = { Text("Image") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        pupilData?.let { currentPupil ->
                            val updatedStudent = currentPupil.copy(
                                name = name,
                                country = country,
                                longitude = lng.toDoubleOrNull() ?: 0.0,
                                latitude = lat.toDoubleOrNull() ?: 0.0,
                                image = image
                            )
                            viewModel.updatePupil(studentId, updatedStudent)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = name.isNotBlank() && country.isNotBlank() &&
                            lat.isNotBlank() && lng.isNotBlank() && pupilData != null
                ) {

                    when(updatePupilViewModel) {
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
                            Text("Save Changes")
                        }
                    }

                }
            }
        }

    }
}