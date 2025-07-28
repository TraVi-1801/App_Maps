package com.vic.project.app_maps.presentation.permission

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.vic.project.app_maps.utils.ContextUtils.hasBackgroundLocation

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BackgroundLocationPermission(onHasPermission: () -> Unit = {}) {
    if (LocalInspectionMode.current || Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return

    val foregroundPermissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val backgroundPermission = Manifest.permission.ACCESS_BACKGROUND_LOCATION

    val foregroundPermissionState = rememberMultiplePermissionsState(foregroundPermissions)
    val backgroundPermissionState = rememberMultiplePermissionsState(
        if (foregroundPermissionState.allPermissionsGranted) {
            listOf(backgroundPermission)
        } else foregroundPermissions + backgroundPermission
    )

    LaunchedEffect(backgroundPermissionState.permissions.map { it.status }) {
        val anyDenied =
            backgroundPermissionState.permissions.any { it.status is PermissionStatus.Denied }

        if (anyDenied) {
            backgroundPermissionState.launchMultiplePermissionRequest()
        } else if (backgroundPermissionState.allPermissionsGranted) {
            onHasPermission.invoke()
        }
    }
}


fun requestBackgroundLocationPermission(
    activity: Activity,
    onRequest: (String) -> Unit,
    onGranted: () -> Unit,
    onDenied: () -> Unit
) {
    val permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        when {
            activity.hasBackgroundLocation() -> {
                onGranted()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) -> {
                AlertDialog.Builder(activity)
                    .setTitle("Permission Request")
                    .setMessage("To continue using this feature, please allow the app full access.")
                    .setPositiveButton("Give Permission") { _, _ ->
                        onRequest.invoke(permission)
                    }
                    .setNegativeButton("Denied") { _, _ ->
                        onDenied()
                    }
                    .show()
            }

            else -> {
                onRequest.invoke(permission)
            }
        }
    } else {
        onGranted()
    }
}
