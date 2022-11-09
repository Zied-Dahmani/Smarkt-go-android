package com.esprit.smarktgo.utils

import com.esprit.smarktgo.model.User
import retrofit2.Call
import  retrofit2.Response
import retrofit2.http.*


interface ApiInterface {

    @POST("user/signUp")
    suspend fun signUp(@Body user: User): Response<User>
   @POST("user/signIn")
   suspend fun signIn(@Body user: User):Response<User>




}