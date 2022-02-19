package io.androidalatan.jsonparser

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.androidalatan.jsonparser.api.JsonParser
import java.lang.reflect.Type

class JsonParserImpl(private val moshi: Moshi) : JsonParser {
    override fun <T> fromJson(jsonString: String, clazz: Class<T>): T? {
        return moshi.adapter(clazz)
            .fromJson(jsonString)
    }

    @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
    override fun <T> toJson(instance: T): String {
        if (instance == null) {
            return ""
        }
        return moshi.adapter<T>(instance!!::class.java)
            .toJson(instance)
    }

    override fun getRawType(type: Type): Class<*> {
        return Types.getRawType(type)
    }
}