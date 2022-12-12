package com.zuku.smartbill.zukufiber.data.services

import Packages
import Push
import com.zuku.smartbill.zukufiber.data.models.response.Json4Kotlin_Base

class Const  private constructor() {

    var json4Kotlin_Base: Json4Kotlin_Base? = null
    private var push: Push? = null
    private var packages:  List<Packages>? = null

    @JvmName("setJson4Kotlin_Base1")
    fun setJson4Kotlin_Base(responseLogin: Json4Kotlin_Base) {
        this.json4Kotlin_Base = responseLogin
    }

    @JvmName("getResponseLogin1")
    fun getJson4Kotlin_Base(): Json4Kotlin_Base? {
        return this.json4Kotlin_Base
    }


    @JvmName("setPushRes")
    fun setPush(push: Push) {
        this.push = push
    }

    @JvmName("getPushRes")
    fun getPush(): Push? {
        return this.push
    }

    @JvmName("setPackages")
    fun setPackages(packages:  List<Packages>) {
        this.packages = packages
    }

    @JvmName("getPackages")
    fun getPackages():  List<Packages>? {
        return this.packages
    }




    private fun Const() {
        init()
    }

    private fun init() {
        this.getJson4Kotlin_Base()
        this.getPackages()
        this.getPush()
    }

    fun getInstance(): Const {
        return ConstHolder.INSTANCE
    }

    object ConstHolder {
        internal val INSTANCE = Const()
    }
}