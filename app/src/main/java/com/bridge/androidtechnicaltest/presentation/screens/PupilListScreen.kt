package com.bridge.androidtechnicaltest.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bridge.androidtechnicaltest.data.model.Pupil
import com.bridge.androidtechnicaltest.presentation.viewmodel.PupilViewModel
import org.koin.androidx.compose.koinViewModel
import com.bridge.androidtechnicaltest.utils.ResultHandler

@Composable
fun PupilListScreen(
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToCreate: () -> Unit,
    viewModel: PupilViewModel = koinViewModel()
) {

    val pupils by viewModel.pupils.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreate
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Student")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            pupils.let { it ->
                when (it) {
                    is ResultHandler.Success -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LazyColumn(
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(16.dp)
                            ) {
                                items(it.data.items) { student ->
                                    Column(
                                        modifier = Modifier.clickable {
                                            //onNavigateToDetail(student.id)
                                        }
                                    ) {
                                        StudentListItem(
                                            student = student,
                                            onItemClick = {
                                                student.let { pupil ->
                                                    if (pupil.pupilId != null) {
                                                        onNavigateToDetail(pupil.pupilId!!)
                                                    }
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                            Row (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = "Page: ${it.data.pageNumber}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = {
                                        println("loading previous pupil page here")
                                        viewModel.loadPreviousPage()
                                    },
                                    enabled = it.data.items.isNotEmpty()
                                ) {
                                    Text("Previous")
                                }
                                Button(
                                    onClick = {
                                        println("loading next pupil page here")
                                        viewModel.loadNextPage()
                                    },
                                    enabled = it.data.items.isNotEmpty()
                                ) {
                                    Text("Next")
                                }
                            }

                            Spacer(modifier = Modifier.height(104.dp))

                        }
                    }

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
                                onClick = { viewModel.loadStudents() }
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
                }
            }

        }
    }
}

@Composable
fun StudentListItem(
    student: Pupil,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onItemClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = student.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Country: ${student.country}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}