package com.vic.project.app_maps.data.remote.model.response

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error(val code: String? = null, val message: String? = null,val data : String? = null) : ResultWrapper<Nothing>()
    object Loading : ResultWrapper<Nothing>()
}
