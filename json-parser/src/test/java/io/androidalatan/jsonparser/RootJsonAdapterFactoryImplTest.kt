package io.androidalatan.jsonparser

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import io.androidalatan.json.moshi.adapter.JsonAdapterFactory
import io.androidalatan.jsonparser.mock.TestPersonObj
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.lang.reflect.Type

internal class RootJsonAdapterFactoryImplTest {

    private val moshi = Moshi.Builder()
        .build()

    @Test
    fun `create empty provider`() {

        val jsonAdapter = RootJsonAdapterFactoryImpl { emptySet() }
            .create(TestPersonObj::class.java, hashSetOf(), moshi)

        Assertions.assertNull(jsonAdapter)
    }

    @Test
    fun `create happy path`() {

        val mockJsonAdapter = object : JsonAdapter<Any?>() {
            override fun fromJson(reader: JsonReader): Any? = null

            override fun toJson(writer: JsonWriter, value: Any?) = Unit
        }

        val jsonAdapter = RootJsonAdapterFactoryImpl { setOf<JsonAdapterFactory>(object : JsonAdapterFactory {
            override fun acceptable(originType: Type): Boolean = originType == TestPersonObj::class.java

            override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
                return mockJsonAdapter
            }
        }) }
            .create(TestPersonObj::class.java, hashSetOf(), moshi)

        Assertions.assertEquals(jsonAdapter, mockJsonAdapter)
    }

    @Test
    fun `create not proper adapter`() {

        val mockJsonAdapter = object : JsonAdapter<Any?>() {
            override fun fromJson(reader: JsonReader): Any? = null

            override fun toJson(writer: JsonWriter, value: Any?) = Unit
        }

        val jsonAdapter = RootJsonAdapterFactoryImpl { setOf<JsonAdapterFactory>(object : JsonAdapterFactory {
            override fun acceptable(originType: Type): Boolean = false

            override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
                return mockJsonAdapter
            }
        }) }
            .create(TestPersonObj::class.java, hashSetOf(), moshi)

        Assertions.assertNull(jsonAdapter)
    }
}