package com.vic.project.app_maps.data.remote.model.response

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import java.util.Objects

open class BaseResponse {
    @SerializedName("predictions")
    val predictions: JsonElement? = null
    @SerializedName("results")
    val results: JsonElement? = null
    @SerializedName("routes")
    val routes: JsonElement? = null
    @SerializedName("messages")
    val message: String = ""
    @SerializedName("code")
    val code: String? = null
    @SerializedName("status")
    val status: String? = null

    fun data(): String {
        return Objects.toString(predictions?:results?:routes)
    }

    val dataObject: JsonObject
        get() = (predictions?:results?:routes)!!.asJsonObject

    val dataArray: JsonArray
        get() = (predictions?:results?:routes)!!.asJsonArray

}