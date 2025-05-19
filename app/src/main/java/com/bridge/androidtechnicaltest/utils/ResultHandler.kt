package com.bridge.androidtechnicaltest.utils

sealed class ResultHandler<out T> {
    data class Success<T>(val data: T) : ResultHandler<T>()
    data class Error(val exception: Exception) : ResultHandler<Nothing>()
    data object Loading : ResultHandler<Nothing>()
}