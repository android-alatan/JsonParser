package io.androidalatan.json.moshi.adapter.sample

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PersonInfo(
    val age: Int,
    val name: String,
    val home: String
)