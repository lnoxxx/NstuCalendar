package com.lnoxdev.data

import android.content.Context
import androidx.room.Room
import com.lnoxdev.data.appSettings.SettingsManager
import com.lnoxdev.data.appSettings.dataStore
import com.lnoxdev.data.neti.NetiScheduleRepository
import com.lnoxdev.data.neti.NetiApi
import com.lnoxdev.data.neti.netiFindGroupDataSource.NetiFindGroupRepository
import com.lnoxdev.data.neti.netiScheduleDataSource.NetiScheduleLoader
import com.lnoxdev.data.neti.netiScheduleDatabase.ScheduleDao
import com.lnoxdev.data.neti.netiScheduleDatabase.ScheduleDatabase
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
        scheduleDao: ScheduleDao,
    ): NetiScheduleLoader = NetiScheduleLoader(netiApi, scheduleDao)

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
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsManager =
        SettingsManager(context.dataStore)

    @Provides
    @Singleton
    fun provideNetiScheduleRepository(
        scheduleDao: ScheduleDao,
        netiScheduleLoader: NetiScheduleLoader,
        settingsManager: SettingsManager,
    ): NetiScheduleRepository =
        NetiScheduleRepository(scheduleDao, netiScheduleLoader, settingsManager)

    @Provides
    @Singleton
    fun provideNetiFindGroupRepository(netiApi: NetiApi): NetiFindGroupRepository =
        NetiFindGroupRepository(netiApi)
}