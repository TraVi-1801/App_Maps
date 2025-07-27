package com.vic.project.app_maps.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.vic.project.app_maps.data.remote.model.response.BaseResponse
import com.vic.project.app_maps.data.remote.model.response.ResultWrapper
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

open class BaseViewModel<State : Any, Event>(initialState: State) : ViewModel() {
    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.tag("CoroutineExceptionHandler").e("CoroutineExceptionHandler got $exception")
    }
    private val viewModelJob = SupervisorJob()
    val ioScope = CoroutineScope(Dispatchers.IO + viewModelJob + handler)
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob + handler)


    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    open fun handleEvent(event: Event) {
        // function for handle view's event
    }

    open fun updateUiState(uiState: State) {
        _uiState.update { uiState }
    }

    fun Flow<ResultWrapper<BaseResponse>>.collectData(
        doOnSuccess: (String) -> Unit
    ) {
        async {
            collect {
                when (it) {
                    is ResultWrapper.Success -> {
                        doOnSuccess.invoke(it.value.data())
                    }
                    else -> Unit
                }
            }
        }
    }


    protected inline fun async(crossinline block: suspend () -> Unit) =
        ioScope.launch {
            block()
        }

    protected inline fun asyncUi(crossinline block: suspend () -> Unit) =
        uiScope.launch {
            block()
        }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}