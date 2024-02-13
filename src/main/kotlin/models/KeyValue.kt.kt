package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class KeyValue(val key: String, val value: String)

val keyValueStorage = mutableMapOf<String, KeyValue>()
