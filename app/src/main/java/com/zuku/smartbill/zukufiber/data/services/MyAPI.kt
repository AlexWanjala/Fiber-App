package com.zuku.smartbill.zukufiber.data.services


import com.zuku.smartbill.zukufiber.data.models.Comments
import retrofit2.Response
import retrofit2.http.GET


interface MyAPI {
    @GET("/comments")
   suspend fun getComments(): Response<List<Comments>>

}
