package com.ponomarenko.livestreaming.data.network

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.ponomarenko.livestreaming.BuildConfig
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


interface RetrofitService {

    @GET("load/loadPosts.php?loadPostMode=standard&loadPostNSFW=0&loadPostOrder=hot&loadPostPage=0&loadPostTimeFrame=day")
    fun getFailsWeekAsync(): Deferred<Response<ResponseBody>>

    @GET("post/{postId}")
    fun getPostsAsync(@Path("postId") postId: Long): Deferred<Response<ResponseBody>>

    companion object {
        operator fun invoke(): RetrofitService {

            val gson = GsonBuilder()
                .setLenient()
                .create()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RetrofitService::class.java)


        }
    }
}
