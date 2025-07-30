package com.vic.project.app_maps.utils

import android.location.Location.distanceBetween
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.vic.project.app_maps.data.model.Step
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

object MapUtils {

    fun calculateBearing(start: LatLng, end: LatLng): Float {
        val lat1 = Math.toRadians(start.latitude)
        val lon1 = Math.toRadians(start.longitude)
        val lat2 = Math.toRadians(end.latitude)
        val lon2 = Math.toRadians(end.longitude)

        val dLon = lon2 - lon1
        val y = sin(dLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLon)
        val bearing = Math.toDegrees(atan2(y, x))
        return ((bearing + 360) % 360).toFloat()
    }


    fun getRemainingPolylinePoints(
        fullPolylinePoints: List<LatLng>,
        current: LatLng
    ): List<LatLng> {
        val nearestIndex = fullPolylinePoints.indexOfNearest(current)
        return if (nearestIndex != -1) {
            fullPolylinePoints.subList(nearestIndex, fullPolylinePoints.size)
        } else {
            fullPolylinePoints
        }
    }

    fun List<LatLng>.indexOfNearest(target: LatLng): Int {
        var minDistance = Double.MAX_VALUE
        var index = -1

        forEachIndexed { i, point ->
            val distance = SphericalUtil.computeDistanceBetween(point, target)
            if (distance < minDistance) {
                minDistance = distance
                index = i
            }
        }

        return index
    }


    fun isOffRoute(current: LatLng, stepPolyline: List<LatLng>, thresholdMeters: Double = 30.0): Boolean {
        val closestDistance = stepPolyline.minOf { point ->
            SphericalUtil.computeDistanceBetween(current, point)
        }
        return closestDistance > thresholdMeters
    }

    fun getNextStep(current: LatLng, steps: List<Step>): Step? {
        return steps.minByOrNull {
            distanceBetween(current, it.start_location?.convertLatLng())
        }?.takeIf {
            distanceBetween(current, it.start_location?.convertLatLng()) < 30  // ngưỡng 30m
        }
    }

    fun distanceBetween(p1: LatLng?, p2: LatLng?): Float {
        if (p1 == null || p2 == null) return 0f
        val result = FloatArray(1)
        distanceBetween(
            p1.latitude, p1.longitude,
            p2.latitude, p2.longitude,
            result
        )
        return result[0]
    }
    fun decodePoly(encoded: String?): List<LatLng> {
        if (encoded == null) return emptyList()

        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }

        return poly
    }
}