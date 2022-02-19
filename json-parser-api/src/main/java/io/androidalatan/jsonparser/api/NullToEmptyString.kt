package io.androidalatan.jsonparser.api

import com.squareup.moshi.JsonQualifier

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class NullToEmptyString