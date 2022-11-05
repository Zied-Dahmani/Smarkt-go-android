package com.esprit.smarktgo.utils

import com.esprit.smarktgo.model.User
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


interface ApiInterface {

    @POST("login")
    fun signUp(@Query("id") id: String, @Query("fullName") fullName: String,@Query("wallet") wallet:Float): Call<User>



    companion object {

        var BASE_URL = "http://192.168.1.22:5000/"

        fun create() : ApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(ApiInterface::class.java)
        }
    }
}