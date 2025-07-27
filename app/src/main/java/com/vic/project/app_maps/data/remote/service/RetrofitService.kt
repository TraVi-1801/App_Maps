package com.vic.project.app_maps.data.remote.service

import com.vic.project.app_maps.data.remote.model.request.KeyRequest
import com.vic.project.app_maps.data.remote.model.response.BaseResponse
import com.vic.project.app_maps.data.remote.model.response.ResultWrapper
import com.vic.project.app_maps.utils.AppConstant
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RetrofitService @Inject constructor(
    private val apiService: ApiService,
) {
    companion object {
        const val TIME_OUT = 30000L
        const val LONG_TIME_OUT = 60000L
    }

    suspend fun getMethod(
        headers: Map<String, String>,
        request: KeyRequest,
        message: Any? = null,
        codeRequired: String
    ): ResultWrapper<BaseResponse> {
        return safeApiCall({
            apiService.get(headers, request.url + message)
        }, codeRequired)
    }

    suspend fun getMethod(
        headers: Map<String, String>,
        request: KeyRequest,
        params: Map<String, String>,
        codeRequired: String
    ): ResultWrapper<BaseResponse> {
        return safeApiCall({
            apiService.get(headers, request.url, params)
        }, codeRequired)
    }

    suspend fun postMethod(
        headers: Map<String, String>,
        request: KeyRequest,
        message: Any? = null,
        codeRequired: String
    ): ResultWrapper<BaseResponse> {
        return safeApiCall({
            apiService.post(headers, request.url, message)
        }, codeRequired)
    }

    suspend fun putMethod(
        headers: Map<String, String>,
        request: KeyRequest,
        message: Any? = null,
        codeRequired: String
    ): ResultWrapper<BaseResponse> {
        return safeApiCall({
            apiService.put(headers, request.url, message)
        }, codeRequired)
    }

    suspend fun deleteMethod(
        headers: Map<String, String>,
        request: KeyRequest,
        message: Any? = null,
        codeRequired: String
    ): ResultWrapper<BaseResponse> {
        return safeApiCall({
            apiService.delete(headers, request.url, message)
        }, codeRequired)
    }

    private suspend fun safeApiCall(
        apiCall: suspend () -> BaseResponse, codeRequired: String
    ): ResultWrapper<BaseResponse> = withTimeoutOrNull(TIME_OUT) {
        try {
            val response = apiCall.invoke()
            if (response.code == codeRequired || response.code == null) {
                ResultWrapper.Success(response)
            } else {
                ResultWrapper.Error(response.code, response.message, response.data())
            }
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> {
                    if (throwable.message == "NO_INTERNET") {
                        ResultWrapper.Error("NO_INTERNET", "NO_INTERNET")
                    } else {
                        ResultWrapper.Error(AppConstant.IOException, throwable.message)
                    }
                }

                is HttpException -> {
                    val code = throwable.code()
                    ResultWrapper.Error(code.toString(), throwable.message)
                }

                is TimeoutCancellationException -> {
                    ResultWrapper.Error(
                        AppConstant.TimeoutCancellationException, throwable.message
                    )
                }

                else -> {
                    ResultWrapper.Error(AppConstant.UnknownError, throwable.message)
                }
            }
        }
    } ?: ResultWrapper.Error(AppConstant.TimeOut, AppConstant.TimeOut)


    private suspend fun safeApiCallForLongTask(
        apiCall: suspend () -> BaseResponse, codeRequired: String
    ): ResultWrapper<BaseResponse> = withTimeoutOrNull(LONG_TIME_OUT) {
        try {
            val response = apiCall.invoke()
            if (response.code == codeRequired) {
                ResultWrapper.Success(response)
            } else {
                ResultWrapper.Error(response.code, response.message)
            }
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> {
                    if (throwable.message == "NO_INTERNET") {
                        ResultWrapper.Error("NO_INTERNET", "NO_INTERNET")
                    } else {
                        ResultWrapper.Error(AppConstant.IOException, throwable.message)
                    }
                }

                is HttpException -> {
                    val code = throwable.code()
                    ResultWrapper.Error(code.toString(), throwable.message)
                }

                is TimeoutCancellationException -> {
                    ResultWrapper.Error(
                        AppConstant.TimeoutCancellationException, throwable.message
                    )
                }

                else -> {
                    ResultWrapper.Error(AppConstant.UnknownError, throwable.message)
                }
            }
        }
    } ?: ResultWrapper.Error(AppConstant.TimeOut, AppConstant.TimeOut)
}