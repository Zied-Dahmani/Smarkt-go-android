package com.esprit.smarktgo.repository

import android.content.ContentValues
import android.util.Log
import com.esprit.smarktgo.model.Order
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.utils.ApiInterface
import com.esprit.smarktgo.utils.RetrofitInstance
import retrofit2.Response

class OrderRepository {

    val api = RetrofitInstance.getRetroInstance().create(ApiInterface::class.java)

    suspend fun addToCart(order: Order): Response<Order>?
    {
        val request =api.addToCart(order)

        if (request.code()!=200 && request.code()!=201 && request.code()!=400) return null
        return request
    }

    suspend fun get(userId: String): Order?
    {
        val request =api.getOrder(User(userId,"",0.0))

        if (request.code()!=200) return null
        return request.body()
    }

    suspend fun removeItem(order: Order): Response<Order>?
    {
        val request =api.removeItem(order)

        if (request.code()!=200) return null
        return request
    }

    suspend fun deleteOrder(user: User): Response<Order>?
    {
        val request =api.deleteOrder(user)

        if (request!!.code()!=200) return null
        return request
    }

}