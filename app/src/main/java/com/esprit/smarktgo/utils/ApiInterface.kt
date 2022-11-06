package com.esprit.smarktgo.utils

import com.esprit.smarktgo.model.User
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers


interface ApiInterface {

    @POST("signUp")
    //fun signUp(@Query("id") id: String, @Query("fullName") fullName: String,@Query("wallet") wallet:Float): Call<User>
  fun signUp(@Body user: User): Call<User>


}