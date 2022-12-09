package com.zuku.smartbill.zukufiber.data.services


import Json4Kotlin_Base
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface MyAPI {
    @FormUrlEncoded
    @POST("index.php")
    suspend fun getSubscriber(@Field("function") function: String, @Field("phoneNumber") phoneNumber: String): Json4Kotlin_Base
}
