package com.example.sbb_hackhealth

data class Announcement (val priority: Int = 0, val payload: Payload? = null) {
    data class Payload (val textDe: String = "", val textEn: String = "", val textIt: String = "", val textFr: String = "")
}
