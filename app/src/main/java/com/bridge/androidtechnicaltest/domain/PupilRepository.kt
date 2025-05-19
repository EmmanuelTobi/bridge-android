package com.bridge.androidtechnicaltest.domain

import com.bridge.androidtechnicaltest.data.model.Pupil
import com.bridge.androidtechnicaltest.data.model.Pupils
import kotlinx.coroutines.flow.Flow

interface PupilRepository {
    suspend fun getStudents(page: Int): Flow<Pupils>
    suspend fun getStudentById(id: Int): Flow<Pupil>
    suspend fun createStudent(student: Pupil): Flow<Pupil>
    suspend fun deleteStudentById(id: Int): Flow<Pupil>
    suspend fun updateStudentById(id: Int, pupil: Pupil): Flow<Pupil>
}