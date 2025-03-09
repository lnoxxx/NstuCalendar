package com.lnoxdev.data

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetiApi {
    @GET("studies/schedule/schedule_classes/schedule")
    suspend fun getSchedule(
        @Query("group") group: String,
        @Query("print") print: String = "true"
    ): Response<ResponseBody>
}