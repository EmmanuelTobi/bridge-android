package com.bridge.androidtechnicaltest.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pupils(
    @SerialName("itemCount")
    val itemCount: Int,
    @SerialName("items")
    val items: List<Pupil>,
    @SerialName("pageNumber")
    val pageNumber: Int,
    @SerialName("totalPages")
    val totalPages: Int
)

@Serializable
data class Pupil(
    @SerialName("country")
    val country: String,
    @SerialName("image")
    val image: String,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("name")
    val name: String,
    @SerialName("pupilId")
    var pupilId: Int? = null
)