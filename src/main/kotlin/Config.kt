package me.orange

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val joinAutoRoles: MutableMap<Long, Long> = mutableMapOf(),
    var lacetoJoinCounter: Int = 0
) {
    companion object {
        const val DIOSLACETO_ID = 565160981949448212 // DiosLaceto
//        const val DIOSLACETO_ID = 889525208950665307 // Orange__04
        const val ORANGE_ID = 452207621668339723 // Orange__03
    }
}