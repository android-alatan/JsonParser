package io.androidalatan.jsonparser

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.androidalatan.jsonparser.api.RootJsonAdapterFactory
import io.androidalatan.json.moshi.adapter.JsonAdapterFactory
import java.lang.reflect.Type
import javax.inject.Provider

class RootJsonAdapterFactoryImpl(_mapperProvider: Provider<Set<JsonAdapterFactory>>) :
    RootJsonAdapterFactory {
    private val mapperProvider by lazy { _mapperProvider.get() }

    override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
        if (annotations.isNotEmpty()) return null
        val clazz = Types.getRawType(type)
        if (clazz.isPrimitive) {
            return null
        }

        return mapperProvider.firstOrNull { factory -> factory.acceptable(type) }
            ?.create(type, annotations, moshi)
    }
}