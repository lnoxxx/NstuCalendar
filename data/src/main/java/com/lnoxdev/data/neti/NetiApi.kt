package com.lnoxdev.data.neti

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetiApi {
    @GET("studies/schedule/schedule_classes/schedule")
    suspend fun getSchedule(
        @Query("group") group: String,
        @Query("print") print: String = "true",
        @Query("week") week: String = "1",
    ): Response<ResponseBody>

    @GET("studies/schedule/schedule_classes/schedule")
    suspend fun getScheduleWeek(
        @Query("group") group: String,
        @Query("week") week: String = "1",
    ): Response<ResponseBody>

    @GET("studies/schedule/schedule_classes")
    suspend fun getGroupSearchResult(@Query("query") searchText: String): Response<ResponseBody>
}