package com.yoloroy.disastersMonitor.web

import com.yoloroy.disastersMonitor.models.Disaster
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface Link {
    @GET("all")
    fun getAllDisasters(): Call<List<Disaster>>

    @POST("add")
    fun addDisaster(@Body disaster: Disaster): Call<Any>

    @Multipart
    @POST("upload")
    fun uploadImage(@Part img: MultipartBody.Part): Call<Any?>
}
