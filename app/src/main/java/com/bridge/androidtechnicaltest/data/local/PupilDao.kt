package com.bridge.androidtechnicaltest.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PupilDao {
    @Query("SELECT * FROM pupils")
    fun getAllPupils(): Flow<List<PupilEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPupil(pupil: PupilEntity)

    @Query("SELECT * FROM pupils WHERE pupilId = :pupilId")
    suspend fun getPupilById(pupilId: Int): PupilEntity?

    @Update
    suspend fun updatePupil(pupil: PupilEntity)

    @Delete
    suspend fun deletePupil(pupil: PupilEntity)

    @Query("DELETE FROM pupils")
    suspend fun deleteAllPupils()
}