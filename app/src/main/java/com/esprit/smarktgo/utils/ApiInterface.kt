package com.esprit.smarktgo.utils

import com.esprit.smarktgo.model.Supermarket
import com.esprit.smarktgo.model.User
import retrofit2.Call
import retrofit2.Callback
import  retrofit2.Response
import retrofit2.http.*


interface ApiInterface {

    @POST("user/signUp")
    suspend fun signUp(@Body user: User): Response<User>

    @POST("user/signIn")
    suspend fun signIn(@Body user: User):Response<User>

    @GET("supermarket/")
    suspend fun getAll():Response<MutableList<Supermarket>>

    @GET("supermarket/getCategories")
    suspend fun getCategories():Response<MutableList<String>>

    @POST("supermarket/")
    suspend fun getNearest(@Body coordinates:ArrayList<Int>):Response<MutableList<Supermarket>>

    @PUT("user/update")
    suspend fun updateProfile(@Body user:User):Response<User>

    @POST("user/signIn")
    fun getInfo(@Body user: User): Call<User>

}