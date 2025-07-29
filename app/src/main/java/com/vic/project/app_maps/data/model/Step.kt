package com.vic.project.app_maps.data.model

data class Step(
    val distance: Distance? = null,
    val duration: Duration? = null,
    val end_location: Location? = null,
    val html_instructions: String? = null,
    val maneuver: String? = null,
    val polyline: Polyline? = null,
    val start_location: Location? = null,
    val travel_mode: String? = ""
)