package com.bridge.androidtechnicaltest.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bridge.androidtechnicaltest.data.model.Pupil
import com.bridge.androidtechnicaltest.presentation.viewmodel.PupilViewModel
import com.bridge.androidtechnicaltest.utils.ResultHandler
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PupilDetailScreen(
    studentId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Pupil) -> Unit,
    viewModel: PupilViewModel = koinViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.getPupilDetails(studentId)
    }

    val student by viewModel.selectedPupil.collectAsState()
    val deleteResult by viewModel.deletePupil.collectAsState()
    
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(deleteResult) {
        if (deleteResult is ResultHandler.Success) {
            onNavigateBack()
        }
    }

    var showErrorSnackbar by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    LaunchedEffect(deleteResult) {
        if (deleteResult is ResultHandler.Error) {
            errorMessage = (deleteResult as ResultHandler.Error).exception.message ?: "Unknown error occurred"
            showErrorSnackbar = true
        }
    }
    
    if (showErrorSnackbar) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                TextButton(onClick = { showErrorSnackbar = false }) {
                    Text("Dismiss")
                }
            },
            dismissAction = {
                IconButton(onClick = { showErrorSnackbar = false }) {
                    Text("×")
                }
            }
        ) {
            Text("Error: $errorMessage")
        }
    }


    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this student?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deletePupilById(studentId)
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Student Details") },
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
            student.let { it ->
                when (it) {
                    is ResultHandler.Error -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Error: ${it.exception.message}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                            Button(
                                onClick = { viewModel.getPupilDetails(studentId) }
                            ) {
                                Text("Retry")
                            }
                        }
                    }

                    is ResultHandler.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    is ResultHandler.Success -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            DetailItem("Name", it.data.name)
                            DetailItem("Country", it.data.country)
                            Spacer(modifier = Modifier.height(16.dp))
                            Row {
                                Button(
                                    onClick = {
                                        onNavigateToEdit(it.data)
                                    }
                                ) {
                                    Text("Edit")
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Button(
                                    onClick = { showDeleteDialog = true },
                                    enabled = deleteResult !is ResultHandler.Loading,
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Delete")
                                        if (deleteResult is ResultHandler.Loading) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(16.dp),
                                                color = MaterialTheme.colorScheme.onError,
                                                strokeWidth = 2.dp
                                            )
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}