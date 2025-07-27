package com.vic.project.app_maps.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InternetAvailabilityRepository @Inject constructor(
    networkStatusTracker: NetworkMonitor
) {
    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean> = _isConnected
    init {
        CoroutineScope(Dispatchers.Default).launch {
            networkStatusTracker.isOnline.collect {
                _isConnected.value = it
            }
        }
    }
}