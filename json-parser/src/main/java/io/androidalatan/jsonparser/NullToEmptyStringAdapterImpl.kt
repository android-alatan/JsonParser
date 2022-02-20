package io.androidalatan.jsonparser

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.ToJson
import io.androidalatan.jsonparser.api.NullToEmptyString
import io.androidalatan.jsonparser.api.NullToEmptyStringAdapter

// Reference : https://github.com/square/moshi/issues/522
class NullToEmptyStringAdapterImpl : NullToEmptyStringAdapter {
    @ToJson
    fun toJson(@NullToEmptyString value: String?): String {
        return value ?: ""
    }

    @FromJson
    @NullToEmptyString
    fun fromJson(reader: JsonReader): String {
        val result = if (reader.peek() === JsonReader.Token.NULL) {
            reader.nextNull()
        } else {
            reader.nextString()
        }

        return result ?: ""
    }
}