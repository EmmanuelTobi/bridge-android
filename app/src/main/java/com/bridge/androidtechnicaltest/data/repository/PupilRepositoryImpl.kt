package com.bridge.androidtechnicaltest.data.repository

import android.util.Log
import com.bridge.androidtechnicaltest.data.api.PupilApiService
import com.bridge.androidtechnicaltest.data.model.Pupil
import com.bridge.androidtechnicaltest.data.model.Pupils
import com.bridge.androidtechnicaltest.domain.PupilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import retrofit2.HttpException
import java.io.IOException
class PupilRepositoryImpl(
    private val apiService: PupilApiService
) : PupilRepository {
    private val TAG = "StudentRepositoryImpl"

    override suspend fun getStudents(page: Int): Flow<Pupils> {
        return flow {
            val res = apiService.getStudents(page)
            emit(res)
        }.retry(3) { cause ->

            Log.d(TAG, "getStudents Retrying: ${cause.message}")
            cause is IOException ||
                    (cause is HttpException && cause.code() != 201) ||
                    (cause is HttpException && cause.code() != 200)

        }.catch { throwable ->

            Log.d(TAG, "getStudents Error: ${throwable.message}")
            when (throwable) {
                is HttpException -> {
                    if (throwable.code() == 404) {
                        throw throwable
                    }
                }
                else -> {
                    throw throwable
                }
            }
        }
    }

    override suspend fun getStudentById(id: Int): Flow<Pupil> {
        return flow {

            val res = apiService.getStudentById(id)
            emit(res)

        }.retry(2) { cause ->
            Log.d(TAG, "getStudentById Retrying: ${cause.message}")
            cause is IOException ||
                    (cause is HttpException && cause.code() != 201) ||
                    (cause is HttpException && cause.code() != 200)
        }.catch {

            Log.d(TAG, "getStudentById Error: ${it.message}")

            when (it) {
                is HttpException -> {

                }
                else -> {

                }
            }

        }
    }

    override suspend fun createStudent(student: Pupil): Flow<Pupil> {
        return flow {

            val res = apiService.createStudent(student)
            emit(res)

        }.retry(4) { cause ->

            Log.d(TAG, "createStudent Retrying: ${cause.message}")
            cause is IOException ||
                    (cause is HttpException && cause.code() != 201)

        }.catch {

            Log.d(TAG, "createStudent Error: ${it.message}")
            when(it) {
                is HttpException -> {

                }
                else -> {

                }
            }

        }
    }

    override suspend fun deleteStudentById(id: Int): Flow<Pupil> {
        return flow {
            val res = apiService.deleteStudentById(id)
            emit(res)
        }.retry {
            Log.d(TAG, "deleteStudentById Retrying: ${it.message}")
            it is IOException ||
                    (it is HttpException && it.code() != 201)
        }.catch {
            Log.d(TAG, "deleteStudentById Error: ${it.message}")
        }
    }

    override suspend fun updateStudentById(id: Int, pupil: Pupil): Flow<Pupil> {
        return flow {
            val res = apiService.getStudentById(id, pupil)
            emit(res)
        }
    }

}