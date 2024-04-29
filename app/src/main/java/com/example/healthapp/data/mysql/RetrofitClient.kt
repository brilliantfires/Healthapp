package com.example.healthapp.data.mysql

import com.example.healthapp.data.LocalDateTimeAdapter
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

object RetrofitClient {
    private const val BASE_URL = "http://192.168.155.248:8080/" // 适用于本地开发环境

    val instance: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            // 使用协程适配器来实现数据传输
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    // 在从MySQL中读取数据时，进行类型转换
    val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    // 使用网络协议来实现从SpringBoot开启的Tomcat服务中读取数据，其中读取到的数据，使用的是gson格式
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(ApiService::class.java)
    }
}
