package com.darothub.weatherapp

import android.app.Application
import com.darothub.weatherapp.data.api.ApiService
import com.darothub.weatherapp.data.api.baseUrl
import com.darothub.weatherapp.data.database.ClimateDao
import com.darothub.weatherapp.data.database.WeatherDatabase
import com.darothub.weatherapp.domain.repository.DataRepository
import com.darothub.weatherapp.domain.repository.LocalDataManager
import com.darothub.weatherapp.domain.repository.RemoteDataManager
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        dao = WeatherDatabase(this).climateDao()
    }
    companion object {
        lateinit var dao: ClimateDao
        private fun getLocalDataManager() = LocalDataManager(dao)
        private fun getRemoteDataManager() = RemoteDataManager(getRetrofit())
        fun getRepository() = DataRepository(getLocalDataManager(), getRemoteDataManager())
        private fun getRetrofit(): ApiService {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .client(OkHttpClient.Builder().build())
                .build()
                .create(ApiService::class.java)
        }
    }
}
