package com.example.huaheejqc.data

data class User(
    var address : String,
    var contact: String,
    var ic: String,
    var name: String,
    var amount: Number,
    var email: String
) {
    companion object {
        fun from(map: Map<String, Any>): User {
            val address = if (map.containsKey("address")) map["address"] as String else ""
            val contact = if (map.containsKey("contact")) map["contact"] as String else ""
            val ic = if (map.containsKey("ic")) map["ic"] as String else ""
            val name = if (map.containsKey("name")) map["name"] as String else ""
            val amount = if (map.containsKey("amount")) map["amount"] as Number else 0
            val email = if (map.containsKey("email")) map["email"] as String else ""
            return User(address, contact, ic, name, amount as Number, email)
        }
    }
}
