package com.example.huaheejqc.data

data class ChatHistory(val user1: String, val user2: String, val history: Array<ChatMsg>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChatHistory

        if (user1 != other.user1) return false
        if (user2 != other.user2) return false

        return true
    }

    override fun hashCode(): Int {
        var result = user1.hashCode()
        result = 31 * result + user2.hashCode()
        result = 31 * result + history.contentHashCode()
        return result
    }
}
