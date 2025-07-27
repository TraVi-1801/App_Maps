package com.vic.project.app_maps.data.remote.model.request


const val RESPONSE_OK = "200"

enum class KeyRequest(val url: String, val codeResponse: String = RESPONSE_OK) {

    PLACE_AUTO("place/autocomplete/json?"),
    PLACE_SEARCH("place/textsearch/json?"),
}