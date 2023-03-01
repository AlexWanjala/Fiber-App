package com.zuku.smartbill.zukufiber.data.services


import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
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
import java.time.Duration


private const val HOST = "https://fiberapp.zuku.co.ke/"
//Kiambu


@RequiresApi(Build.VERSION_CODES.O)
private val client = OkHttpClient.Builder()
    .connectTimeout(Duration.ofSeconds(30))
    .readTimeout(Duration.ofSeconds(30))
    .writeTimeout(Duration.ofSeconds(30))
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
                           @Field("phoneNumber") phoneNumber: String,
                           @Field("paybill") paybill: String): Json4Kotlin_Base

    @FormUrlEncoded
    @POST("index.php")
    suspend fun checkPayment(@Field("function") function: String,
                             @Field("accNo") accNo: String): Json4Kotlin_Base

    @FormUrlEncoded
    @POST("index.php")
    suspend fun getPackages(@Field("function") function: String,
                             @Field("mbps") mbps: String,
                             @Field("subdb") subdb: String): Json4Kotlin_Base

    @FormUrlEncoded
    @POST("index.php")
    suspend fun getInfo(@Field("function") function: String,
                             @Field("subdb") subdb: String): Json4Kotlin_Base

    @FormUrlEncoded
    @POST("index.php")
    suspend fun getpaymentmethods(@Field("function") function: String,
                                  @Field("subdb") subdb: String): Json4Kotlin_Base

    @FormUrlEncoded
    @POST("index.php")
    suspend fun getSubsTrans(@Field("function") function: String,
                             @Field("subid") subid: String,
                             @Field("subdb") subdb: String): Json4Kotlin_Base

    @FormUrlEncoded
    @POST("index.php")
    suspend fun getInvoices(@Field("function") function: String,
                             @Field("accNo") subdb: String): Json4Kotlin_Base

    @FormUrlEncoded
    @POST("index.php")
    suspend fun sendSMS(@Field("function") function: String,
                             @Field("phoneNumber") phoneNumber: String,
                             @Field("message") message: String): Json4Kotlin_Base

    @FormUrlEncoded
    @POST("index.php")
    suspend fun changePlanRequest(@Field("function") function: String,
                             @Field("title") title : String,
                             @Field("date") date: String,
                             @Field("description") description: String,
                             @Field("amount") amount: String,
                             @Field("subid") subid: String,
                             @Field("subdb") subdb: String,
                             @Field("subName") subName: String,
                             @Field("phoneNumber") phoneNumber: String,
                             @Field("currentPackage") currentPackage: String): Json4Kotlin_Base

    @FormUrlEncoded
    @POST("index.php")
    suspend fun shiftRequest(@Field("function") function: String,
                             @Field("fromName") fromName: String,
                             @Field("date") date: String,
                             @Field("address") address: String,
                             @Field("latLng") latLng: String,
                             @Field("description") description: String,
                             @Field("fromAddress") fromAddress: String,
                             @Field("phoneNumber") phoneNumber: String,
                             @Field("accNo") accNo: String,
                             @Field("subdb") subdb: String): Json4Kotlin_Base

    @FormUrlEncoded
    @POST("index.php")
    suspend fun paymentsoptions(@Field("function") function: String,
                             @Field("subdb") subdb: String): Json4Kotlin_Base

    @FormUrlEncoded
    @POST("index.php")
    suspend fun updateProfile(@Field("function") function: String,
                              @Field("subid") subid: String,
                              @Field("subdb") subdb: String,
                              @Field("taxpin") taxpin: String,
                              @Field("email") email: String,
                              @Field("phoneNumber") phoneNumber: String,
                              @Field("cid") cid: String): Json4Kotlin_Base


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

fun launchSTK(activity: Context) {

    //Remember to allow overlay permission
    activity.startService(Intent(activity, FloatingWindow::class.java))

    val intent = activity.packageManager.getLaunchIntentForPackage("com.android.stk")
    if (intent == null) {
        try {
            val intent1 = Intent()
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent1.addCategory("android.intent.category.LAUNCHER")
            intent1.action = "android.intent.action.MAIN"
            intent1.type = "text/plain"
            intent1.component =
                ComponentName("com.android.stk", "com.android.stk.StkLauncherActivity")
            activity.startActivity(intent1)
            return
        } catch (activitynotfoundexception: ActivityNotFoundException) {
        }
    } else if (intent == null) {
        try {
            val intent2 = Intent()
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent2.addCategory("android.intent.category.LAUNCHER")
            intent2.action = "android.intent.action.MAIN"
            intent2.type = "text/plain"
            intent2.component = ComponentName("com.android.stk", "com.android.stk.StkMain")
            activity.startActivity(intent2)
            return
        } catch (activitynotfoundexception1: ActivityNotFoundException) {
        }
    } else if (intent == null) {
        val intent3 = Intent()
        intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent3.addCategory("android.intent.category.LAUNCHER")
        intent3.action = "android.intent.action.MAIN"
        intent3.type = "text/plain"
        intent3.component = ComponentName("com.android.stk", "com.android.stk.StkLauncherActivity")
        activity.startActivity(intent3)
    } else if (intent == null) {
        val intent4 = Intent()
        intent4.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent4.addCategory("android.intent.category.LAUNCHER")
        intent4.action = "android.intent.action.MAIN"
        intent4.type = "text/plain"
        intent4.component = ComponentName("com.android.stk", "com.android.stk.StkMain")
        activity.startActivity(intent4)
    } else if (intent == null) {
        val launchIntent5 =
            activity.packageManager.getLaunchIntentForPackage("com.mediatek.StkSelection")
        activity.startActivity(launchIntent5)
    } else {
        activity.startActivity(intent)
    }
    return
}



