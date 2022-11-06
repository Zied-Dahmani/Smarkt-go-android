package com.esprit.smarktgo.utils

import com.esprit.smarktgo.model.User
import com.google.android.gms.common.api.Response
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers


interface ApiInterface {

    @POST("user/signUp")
    fun signUp(@Body user: User): Call<User>

   /*@GET("user/signIn")
    fun signIn(@Body user: User): Call<User>
*/
    @GET("user/signIn")
    @Headers("Accept:application/json", "Content-Type:application/json",
        "Authorization: Bearer 73668350bdf06c66f3388408c1a712b378c3e25da599753b21b664a6261e246c")
    fun signIn(@Body user: User): Call<User>

}