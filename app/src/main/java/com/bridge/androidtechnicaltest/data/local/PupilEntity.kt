package com.bridge.androidtechnicaltest.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bridge.androidtechnicaltest.data.model.Pupil

@Entity(tableName = "pupils")
data class PupilEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val country: String,
    val image: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val pupilId: Int?
) {
    fun toPupil(): Pupil = Pupil(
        country = country,
        image = image,
        latitude = latitude,
        longitude = longitude,
        name = name,
        pupilId = pupilId
    )

    companion object {
        fun fromPupil(pupil: Pupil): PupilEntity = PupilEntity(
            country = pupil.country,
            image = pupil.image,
            latitude = pupil.latitude,
            longitude = pupil.longitude,
            name = pupil.name,
            pupilId = pupil.pupilId
        )
    }
}