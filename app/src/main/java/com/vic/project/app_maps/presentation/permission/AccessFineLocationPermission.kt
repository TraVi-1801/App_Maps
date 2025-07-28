package com.vic.project.app_maps.presentation.permission

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalInspectionMode
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermission(onHasPermission: () -> Unit = {}) {
    if (LocalInspectionMode.current) return
    val permissionsList = mutableListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val permissionState = rememberMultiplePermissionsState(permissionsList)

    LaunchedEffect(permissionState.permissions.map { it.status }) {
        val deniedPermissions = permissionState.permissions.filterNot { it.status.isGranted }

        if (deniedPermissions.isNotEmpty()) {
            permissionState.launchMultiplePermissionRequest()
        } else {
            onHasPermission.invoke()
        }
    }
}