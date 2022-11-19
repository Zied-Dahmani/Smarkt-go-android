package com.esprit.smarktgo.utils

import com.esprit.smarktgo.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiInterface {

    @POST("user/signUp")
    suspend fun signUp(@Body user: User): Response<User>

    @POST("user/signIn")
    suspend fun signIn(@Body user: User):Response<User>

    @POST("user/update")
    suspend fun updateProfile(@Body user: User):Response<User>

    @POST("supermarket/")
    suspend fun getNearest(@Body coordinates:ArrayList<Int>):Response<MutableList<Supermarket>>

    @GET("supermarket/")
    suspend fun getAll():Response<MutableList<Supermarket>>

    @GET("supermarket/getCategories")
    suspend fun getCategories():Response<MutableList<String>>

    @POST("supermarket/getFavorites")
    suspend fun getFavorites(@Body user: User):Response<MutableList<Supermarket>>

    @POST("supermarket/isFavorite")
    suspend fun isFavorite(@Body isFavoriteBody: IsFavoriteBody):Response<MutableList<String>>

    @POST("supermarket/addRemoveFavorite")
    suspend fun addRemoveFavorite(@Body addRemoveFavorite: AddRemoveFavorite):Response<AddRemoveFavorite>

    @POST("item/")
    suspend fun getAllBySupermarketIdAndCategory(@Body itemInfo: ItemInfo):Response<MutableList<Item>>

    @POST("order/add")
    suspend fun addToCart(@Body order: Order): Response<Order>

    @POST("order/get")
    suspend fun getOrder(@Body user: User): Response<Order>

    @POST("order/removeItem")
    suspend fun removeItem(@Body order: Order): Response<Order>

    @POST("/order/delete")
    suspend fun deleteOrder(@Body user: User): Response<Order>?


    @POST("user/signIn")
    fun getInfo(@Body user: User): Call<User>

    @PUT("user/update")
    suspend fun updateProfilee(@Body user: User): Response<User>?




}


