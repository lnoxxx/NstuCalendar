package com.lnoxdev.data.netiSchedule.netiScheduleDataSource

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

    @GET("studies/schedule/schedule_classes/schedule")
    suspend fun getScheduleWeek(
        @Query("group") group: String,
        @Query("week") week: String = "1",
    ): Response<ResponseBody>
}