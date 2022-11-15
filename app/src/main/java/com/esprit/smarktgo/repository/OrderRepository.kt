package com.esprit.smarktgo.repository

import com.esprit.smarktgo.model.Order
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

}