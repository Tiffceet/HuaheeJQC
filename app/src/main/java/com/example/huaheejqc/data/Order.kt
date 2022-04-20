package com.example.huaheejqc.data

data class Order(
    var book: String,
    var buyer: String,
    var seller: String,
    var status: String,
    var additional_info: String?,
    var address: String?,
)
