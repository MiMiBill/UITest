package com.example.uitest

class Data {

    var name:String
    set(value) {
        name = value
    }
    get() = name


    val value:Int
    get() = if (value > 0) 1 else 2 ;
}