package com.vic.project.app_maps.data.model

data class DirectionData(
    val legs: List<Leg>? = null,
    val overview_polyline: OverviewPolyline? = null
)