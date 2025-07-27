package com.vic.project.app_maps.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import org.json.JSONObject

object JSON {
    private val parseGson: Gson = GsonBuilder().disableHtmlEscaping().create()

    @Throws(Exception::class)
    fun encode(obj: Any?): String {
        return try {
            parseGson.toJson(obj)
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    @Throws(Exception::class)
    fun encodeListAsString(list: List<String>): String {
        val stringBuilder = StringBuilder("[")

        for ((index, item) in list.withIndex()) {
            stringBuilder.append("\"$item\"")

            if (index < list.size - 1) {
                stringBuilder.append(",")
            }
        }

        stringBuilder.append("]")

        return stringBuilder.toString()
    }

    fun Any?.toJson(): String {
        return try {
            parseGson.toJson(this)
        } catch (e: Exception) {
            return ""
        }
    }

    fun <T> String.decodeTo(tClass: Class<T>): T? {
        return try {
            parseGson.fromJson(this, tClass)
        } catch (e: Exception) {
            null
        }
    }

    fun <T> String.decodeTo(clazz: Class<Array<T>>): List<T> {
        return try {
            decodeToList(this, clazz)
        } catch (e: Exception) {
            ArrayList()
        }
    }

    fun <T> decode(json: String, tClass: Class<T>): T? {
        return try {
            parseGson.fromJson(json, tClass)
        } catch (e: Exception) {
            null
        }
    }

    fun <T> decodeToList(jsonElement: String?, clazz: Class<Array<T>>): List<T> {
        return try {
            val array: Array<T> = parseGson.fromJson(jsonElement, clazz)
            array.toList()
        } catch (e: Exception) {
            ArrayList()
        }
    }

    fun <T> isDecodeToList(jsonElement: String?, clazz: Class<Array<T>>): Boolean {
        return try {
            val array: Array<T> = parseGson.fromJson(jsonElement, clazz)
            array.toList().isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }

    fun <T> decodeListToList(
        jsonElement: String?,
        clazz: Class<Array<Array<T>>>
    ): Array<Array<T>>? {
        return try {
            val array: Array<Array<T>> = parseGson.fromJson(jsonElement, clazz)
            array
        } catch (e: Exception) {
            null
        }
    }

    fun <T> decodeListToList(
        jsonElement: String?,
        clazz: Class<Array<Array<Array<T>>>>
    ): Array<Array<Array<T>>>? {
        return try {
            val array: Array<Array<Array<T>>> = parseGson.fromJson(jsonElement, clazz)
            array
        } catch (e: Exception) {
            null
        }
    }

    fun toJsonElement(data: String?): JsonElement? {
        val parser = JsonParser()
        return parser.parse(data)
    }

    fun evertStringToJSON(json: String?): JSONObject? {
        return try {
            JSONObject(json)
        } catch (e: Exception) {
            null
        }
    }
}