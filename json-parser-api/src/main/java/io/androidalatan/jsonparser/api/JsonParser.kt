package io.androidalatan.jsonparser.api

import java.lang.reflect.Type

interface JsonParser {
    fun <T> fromJson(jsonString: String, clazz: Class<T>): T?
    fun <T> toJson(instance: T): String

    fun getRawType(type: Type): Class<*>
}