package com.zuku.smartbill.zukufiber.data.services

import com.zuku.smartbill.zukufiber.data.models.response.Json4Kotlin_Base

class Const  private constructor() {

    var json4Kotlin_Base: Json4Kotlin_Base? = null

    @JvmName("setJson4Kotlin_Base1")
    fun setJson4Kotlin_Base(responseLogin: Json4Kotlin_Base) {
        this.json4Kotlin_Base = responseLogin
    }


    @JvmName("getResponseLogin1")
    fun getJson4Kotlin_Base(): Json4Kotlin_Base? {
        return this.json4Kotlin_Base
    }

    private fun Const() {
        init()
    }

    private fun init() {
        this.getJson4Kotlin_Base()
    }

    fun getInstance(): Const? {
        return ConstHolder.INSTANCE
    }

    object ConstHolder {
        internal val INSTANCE = Const()
    }
}