package com.bridge.androidtechnicaltest.data.repository

import android.util.Log
import com.bridge.androidtechnicaltest.data.api.PupilApiService
import com.bridge.androidtechnicaltest.data.local.PupilDao
import com.bridge.androidtechnicaltest.data.local.PupilEntity
import com.bridge.androidtechnicaltest.data.model.Pupil
import com.bridge.androidtechnicaltest.data.model.Pupils
import com.bridge.androidtechnicaltest.domain.PupilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry
import retrofit2.HttpException
import java.io.IOException
class PupilRepositoryImpl(
    private val apiService: PupilApiService,
    private val pupilDao: PupilDao
) : PupilRepository {
    private val TAG = "PupilRepositoryImpl"

    override suspend fun getPupils(page: Int): Flow<Pupils> {
        return flow {
            try {

                val apiPupils = apiService.getPupils(page)
                val localPupils = pupilDao.getAllPupils().first().filter { it.pupilId == null } ////load pupils from the room setup db

                if (localPupils.isNotEmpty()) {
                    val mergedItems = apiPupils.items.toMutableList()
                    mergedItems.addAll(localPupils.map { it.toPupil() })
                    
                    emit(Pupils(
                        itemCount = mergedItems.size,
                        items = mergedItems,
                        pageNumber = apiPupils.pageNumber,
                        totalPages = apiPupils.totalPages
                    ))
                } else {
                    emit(apiPupils)
                }

            } catch (throwable: Exception) {
                Log.d(TAG, "getPupils from local after Error: ${throwable.message}")
                try {
                    val localPupils = pupilDao.getAllPupils().first()
                    emit(Pupils(
                        itemCount = localPupils.size,
                        items = localPupils.map { it.toPupil() },
                        pageNumber = page,
                        totalPages = 1
                    ))
                } catch (e: Exception) {
                    throw e
                }
            }
        }.retry(3) { cause ->
            Log.d(TAG, "getPupils Retrying: ${cause.message}")
            cause is IOException ||
                    (cause is HttpException && cause.code() != 201) ||
                    (cause is HttpException && cause.code() != 200)
        }.catch { throwable ->
            Log.d(TAG, "getPupils Error: ${throwable.message}")
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

    override suspend fun getPupilById(id: Int): Flow<Pupil> {
        return flow {

            val res = apiService.getPupilById(id)
            emit(res)

        }.retry(2) { cause ->
            Log.d(TAG, "getPupilById Retrying: ${cause.message}")
            cause is IOException ||
                    (cause is HttpException && cause.code() != 201) ||
                    (cause is HttpException && cause.code() != 200)
        }.catch {

            try {

                val localPupil = pupilDao.getPupilById(id)?.toPupil()
                if (localPupil != null) {
                    emit(localPupil)
                }

            } catch (e: Exception) {
                throw e
            }

            Log.d(TAG, "getPupilById Error: ${it.message}")
            when (it) {
                is HttpException -> {

                }
                else -> {

                }
            }
        }
    }

    override suspend fun createPupil(Pupil: Pupil): Flow<Pupil> {
        return flow {

            val res = apiService.createPupil(Pupil)
            emit(res)

        }.retry(4) { cause ->

            Log.d(TAG, "createPupil Retrying: ${cause.message}")
            cause is IOException ||
                    (cause is HttpException && cause.code() != 201)

        }.catch {

            Log.d(TAG, "createPupil in local after final Error: ${it.message}")
            pupilDao.insertPupil(PupilEntity.fromPupil(Pupil))
            emit(Pupil)

            Log.d(TAG, "createPupil Error: ${it.message}")
            when(it) {
                is HttpException -> {

                }
                else -> {

                }
            }

        }
    }

    override suspend fun deletePupilById(id: Int): Flow<Any> {
        return flow {

            val res = apiService.deletePupil(id)
            pupilDao.getPupilById(id)?.let { pupilDao.deletePupil(it) }
            emit(res)

        }.retry (3) {
            Log.d(TAG, "deletePupilById Retrying: ${it.message}")
            it is IOException ||
                    (it is HttpException && it.code() != 201)
        }.catch {
            Log.d(TAG, "deletePupilById Error: ${it.message}")
        }
    }

    override suspend fun updatePupilById(id: Int, pupil: Pupil): Flow<Pupil> {
        return flow {
            try {
                val res = apiService.updatePupil(id, pupil)
                pupilDao.getPupilById(id)?.let {
                    pupilDao.updatePupil(PupilEntity.fromPupil(pupil))
                }
                emit(res)
            } catch (e: Exception) {
                pupilDao.getPupilById(id)?.let {
                    pupilDao.updatePupil(PupilEntity.fromPupil(pupil))
                    emit(pupil)
                } ?: throw e
            }
        }
    }

}