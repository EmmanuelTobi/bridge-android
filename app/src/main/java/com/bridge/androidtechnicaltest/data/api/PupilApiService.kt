package com.bridge.androidtechnicaltest.data.api

import com.bridge.androidtechnicaltest.data.model.Pupil
import com.bridge.androidtechnicaltest.data.model.Pupils
import retrofit2.http.*

interface PupilApiService {
    @GET("pupils")
    suspend fun getPupils(@Query("page") page: Int): Pupils

    @GET("pupils/{id}")
    suspend fun getPupilById(@Path("id") id: Int): Pupil

    @PUT("pupils/{id}")
    suspend fun updatePupil(@Path("id") id: Int, @Body pupil: Pupil): Pupil

    @DELETE("pupils/{id}")
    suspend fun deletePupil(@Path("id") id: Int): Pupil

    @POST("pupils")
    suspend fun createPupil(@Body student: Pupil): Pupil
}