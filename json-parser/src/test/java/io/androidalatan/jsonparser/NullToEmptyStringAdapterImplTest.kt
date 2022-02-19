package io.androidalatan.jsonparser

import com.squareup.moshi.JsonReader
import io.androidalatan.jsonparser.NullToEmptyStringAdapterImpl
import okio.Buffer
import okio.ByteString.Companion.encodeUtf8
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class NullToEmptyStringAdapterImplTest {

    private val adapter = NullToEmptyStringAdapterImpl()

    @Test
    fun toJson() {
        Assertions.assertEquals("", adapter.toJson(null))
        Assertions.assertEquals("", adapter.toJson(""))
        Assertions.assertEquals("hello", adapter.toJson("hello"))
    }

    @Test
    fun fromJson() {
        val buffer = Buffer()
        buffer.write("null".encodeUtf8())
        buffer.flush()
        buffer.close()
        Assertions.assertEquals("", adapter.fromJson(JsonReader.of(buffer)))
    }

}