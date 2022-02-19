package io.androidalatan.jsonparser

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import io.androidalatan.jsonparser.mock.MockPersonAdapter
import io.androidalatan.jsonparser.mock.TestPersonObj
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class JsonParserImplTest {

    private val personAdapter = MockPersonAdapter()
    private val moshi = Moshi.Builder()
        .add(personAdapter)
        .build()

    private val jsonParser = JsonParserImpl(moshi)

    @AfterEach
    internal fun tearDown() {
        personAdapter.toJsonCount = 0
        personAdapter.fromJsonCount = 0
    }

    @Test
    fun `fromJson happy path`() {
        val name = MOCK_NAME
        val json = "{\"name\":\"$name\"}"
        val person = jsonParser.fromJson(json, TestPersonObj::class.java)
        Assertions.assertNotNull(person)
        Assertions.assertEquals(name, person!!.name)
        Assertions.assertEquals(1, personAdapter.fromJsonCount)
        Assertions.assertEquals(1, personAdapter.completeFromJsonCount)
    }

    @Test
    fun `fromJson null json`() {
        val json = "{\"name\":null}"
        Assertions.assertThrows(JsonDataException::class.java) {
            jsonParser.fromJson(json, TestPersonObj::class.java)
        }
        Assertions.assertEquals(1, personAdapter.fromJsonCount)
        Assertions.assertEquals(0, personAdapter.completeFromJsonCount)
    }

    @Test
    fun toJson() {
        val actualJsonString = jsonParser.toJson(TestPersonObj(MOCK_NAME))
        val expectedJson = "{\"name\":\"$MOCK_NAME\"}"
        Assertions.assertEquals(expectedJson, actualJsonString)

        Assertions.assertEquals(1, personAdapter.toJsonCount)
        Assertions.assertEquals(1, personAdapter.completeToJsonCount)
    }

    companion object {
        private const val MOCK_NAME = "test-1"
    }
}