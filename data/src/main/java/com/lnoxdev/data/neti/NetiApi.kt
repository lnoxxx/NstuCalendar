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
        @Query("week") week: String = "1"
        // я добавил параметр week, что на первый взгляд вообще не логично
        // ведь это расписание на весь семестр, казалось бы - зачем указывать номер недели?
        // но когда начнается сессия, по ✨необъяснимым✨ причинам без этого параметра расписание не отобразится
        // возможно это баг сайта и его когда-то исправят. Но вот уже два года (как минимум) он существует...

    ): Response<ResponseBody>

    @GET("studies/schedule/schedule_classes/schedule")
    suspend fun getScheduleWeek(
        @Query("group") group: String,
        @Query("week") week: String = "1",
    ): Response<ResponseBody>

    @GET("studies/schedule/schedule_classes")
    suspend fun getGroupSearchResult(@Query("query") searchText: String): Response<ResponseBody>
}