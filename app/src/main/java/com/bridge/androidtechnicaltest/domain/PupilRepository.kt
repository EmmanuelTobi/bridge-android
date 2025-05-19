package com.bridge.androidtechnicaltest.domain

import com.bridge.androidtechnicaltest.data.model.Pupil
import com.bridge.androidtechnicaltest.data.model.Pupils
import kotlinx.coroutines.flow.Flow

interface PupilRepository {
    suspend fun getPupils(page: Int): Flow<Pupils>
    suspend fun getPupilById(id: Int): Flow<Pupil>
    suspend fun createPupil(pupil: Pupil): Flow<Pupil>
    suspend fun deletePupilById(id: Int): Flow<Pupil>
    suspend fun updatePupilById(id: Int, pupil: Pupil): Flow<Pupil>
}