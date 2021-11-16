package com.example.uitest

class Person1:IPeople {
    var mFood:String? = null
    override fun eat(food: String) {
        mFood = food
        println("正在吃${mFood}")
    }
    fun eat(){
        println("正在吃${mFood}")
    }

    override fun cry() {
        println("${mFood}正在哭泣")
    }

}