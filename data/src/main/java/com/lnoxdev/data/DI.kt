package com.lnoxdev.data

import android.content.Context
import androidx.room.Room
import com.lnoxdev.data.netiNetworkDataSource.NetiApi
import com.lnoxdev.data.netiSchedule.NetiScheduleLoader
import com.lnoxdev.data.netiSchedule.NetiScheduleOnDatabaseSaver
import com.lnoxdev.data.netiSchedule.netiScheduleDatabase.ScheduleDao
import com.lnoxdev.data.netiSchedule.netiScheduleDatabase.ScheduleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

        return okHttpClient
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val retrofit =
            Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(NETI_BASE_URL)
                .build()

        return retrofit
    }

    @Provides
    @Singleton
    fun provideNetiApi(retrofit: Retrofit): NetiApi {
        return retrofit.create(NetiApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNetiRepository(
        netiApi: NetiApi,
        saver: NetiScheduleOnDatabaseSaver
    ): NetiScheduleLoader = NetiScheduleLoader(netiApi, saver)

    @Provides
    @Singleton
    fun provideScheduleDatabase(@ApplicationContext context: Context): ScheduleDatabase {
        return Room.databaseBuilder(
            context,
            ScheduleDatabase::class.java,
            "schedule_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideScheduleDao(scheduleDatabase: ScheduleDatabase): ScheduleDao =
        scheduleDatabase.scheduleDao()

    @Provides
    @Singleton
    fun provideNetiScheduleOnDatabaseSaver(scheduleDao: ScheduleDao): NetiScheduleOnDatabaseSaver =
        NetiScheduleOnDatabaseSaver(scheduleDao)
}