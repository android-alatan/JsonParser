package io.androidalatan.json.moshi.adapter.sample

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Address(
    val city: Int,
    val street: String,
    val block: String
)