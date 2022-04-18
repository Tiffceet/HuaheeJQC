package com.example.huaheejqc

data class User(
    var address : String,
    var contact: String,
    var ic: String,
    var name: String
) {
    companion object {
        fun from(map: Map<String, Any>): User {
            val address = if (map.containsKey("address")) map["address"] as String else ""
            val contact = if (map.containsKey("contact")) map["contact"] as String else ""
            val ic = if (map.containsKey("ic")) map["ic"] as String else ""
            val name = if (map.containsKey("name")) map["name"] as String else ""
            return User(address, contact, ic, name)
        }
    }
}
