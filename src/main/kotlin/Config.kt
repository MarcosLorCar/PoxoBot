package me.orange

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val joinAutoRoles: MutableMap<Long, Long> = mutableMapOf(),
)