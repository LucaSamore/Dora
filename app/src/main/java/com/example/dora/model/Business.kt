package com.example.dora.model

import arrow.optics.optics
import com.example.dora.common.BusinessPlace
import com.example.dora.common.nowWithPattern

@optics
data class Business(
    val uuid: String? = null,
    val name: String? = null,
    val owner: User? = null,
    val description: String? = null,
    val address: BusinessPlace? = null,
    val website: String? = null,
    val phoneNumber: String? = null,
    val category: Category? = null,
    val amenities: List<String>? = null,
    val timeTable: Map<Days, List<TimeSpan>>? = null,
    @field:JvmField val isOpen: Boolean? = null,
    val images: List<String>? = null,
    val createdAt: String = nowWithPattern("yyyy-MM-dd HH:mm:ss")
) {
    companion object {
        const val collection = "businesses"
    }
}

enum class Days(val day: String) {
    SUNDAY("sunday"),
    MONDAY("monday"),
    TUESDAY("tuesday"),
    WEDNESDAY("wednesday"),
    THURSDAY("thursday"),
    FRIDAY("friday"),
    SATURDAY("saturday"),
}

data class TimeSpan(val start: String, val end: String)
