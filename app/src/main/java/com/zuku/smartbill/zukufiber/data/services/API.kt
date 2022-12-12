package com.zuku.smartbill.zukufiber.data.services


import android.content.Context
import com.yfbx.demo.net.HeaderInterceptor
import com.yfbx.demo.net.LoggerInterceptor
import com.zuku.smartbill.zukufiber.BuildConfig
import com.zuku.smartbill.zukufiber.data.models.response.Json4Kotlin_Base
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

private const val HOST = "https://07f5-154-70-56-156.ap.ngrok.io/apis/fiber-app-api/"


private val client = OkHttpClient.Builder()
    .addInterceptor(HeaderInterceptor())
    .apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(LoggerInterceptor())
        }
    }
    .build()

val api: API = Retrofit.Builder()
    .baseUrl(HOST)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(API::class.java)

interface API {
    @FormUrlEncoded
    @POST("index.php")
    suspend fun getSubscriber(@Field("function") function: String,
                              @Field("phoneNumber") phoneNumber: String): Json4Kotlin_Base
    @FormUrlEncoded
    @POST("index.php")
    suspend fun sendPrompt(@Field("function") function: String,
                           @Field("accNo") accNo : String,
                           @Field("amount") amount: String,
                           @Field("phoneNumber") phoneNumber: String): Json4Kotlin_Base

    @FormUrlEncoded
    @POST("index.php")
    suspend fun checkPayment(@Field("function") function: String,
                             @Field("accNo") accNo: String): Json4Kotlin_Base

    @FormUrlEncoded
    @POST("index.php")
    suspend fun getPackages(@Field("function") function: String,
                             @Field("subdb") subdb: String): Json4Kotlin_Base

}

fun save(context: Context, key: String?, value: String?) {
    val prefs = context.getSharedPreferences("com.zuku.startbill.zukufiber", Context.MODE_PRIVATE)
    val editor = prefs.edit()
    editor.putString(key, value)
    editor.apply()
}

fun getValue(context: Context, key: String?): String? {
    val prefs = context.getSharedPreferences("com.zuku.startbill.zukufiber", Context.MODE_PRIVATE)
    return prefs.getString(key, "")
}


