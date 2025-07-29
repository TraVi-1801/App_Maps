package com.vic.project.app_maps.data.model

data class Leg(
    val distance: Distance? = null,
    val duration: Duration? = null,
    val end_address: String? = null,
    val start_address: String? = null,
    val steps: List<Step>? = null
)