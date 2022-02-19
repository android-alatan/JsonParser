package io.androidalatan.json.moshi.adapter

import com.squareup.moshi.JsonAdapter
import java.lang.reflect.Type

interface JsonAdapterFactory : JsonAdapter.Factory {
    fun acceptable(originType: Type): Boolean
}