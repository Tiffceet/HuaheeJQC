package com.example.huaheejqc

data class User(
    var Address : String,
    var Contact: String,
    var IC: String,
    var Name: String
) {
    companion object {
        fun from(map: Map<String, Any>): User {
            val Address = if (map.containsKey("Address")) map["Address"] as String else ""
            val Contact = if (map.containsKey("Contact")) map["Contact"] as String else ""
            val IC = if (map.containsKey("IC")) map["IC"] as String else ""
            val Name = if (map.containsKey("Name")) map["Name"] as String else ""
            return User(Address, Contact, IC, Name)
        }
    }
}
