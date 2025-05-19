package com.bridge.androidtechnicaltest.data.api

import com.bridge.androidtechnicaltest.data.model.Pupil
import com.bridge.androidtechnicaltest.data.model.Pupils
import retrofit2.http.*

interface PupilApiService {
    @GET("pupils")
    suspend fun getStudents(@Query("page") page: Int): Pupils

    @GET("pupils/{id}")
    suspend fun getStudentById(@Path("id") id: Int): Pupil

    @PUT("pupils/{id}")
    suspend fun getStudentById(@Path("id") id: Int, @Body pupil: Pupil): Pupil

    @DELETE("pupils/{id}")
    suspend fun deleteStudentById(@Path("id") id: Int): Pupil

    @POST("pupils")
    suspend fun createStudent(@Body student: Pupil): Pupil
}