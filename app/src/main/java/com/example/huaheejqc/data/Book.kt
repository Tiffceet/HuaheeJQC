package com.example.huaheejqc.data

import android.icu.text.CaseMap

data class Book(var title: String,
                var author: String,
                var price: String,
                var description: String,
                var category: String,
                var status: String,
                var userid: String,
                var bookid: String = "")
