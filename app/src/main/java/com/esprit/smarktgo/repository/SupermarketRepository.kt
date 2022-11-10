package com.esprit.smarktgo.repository

import com.esprit.smarktgo.model.Supermarket
import com.esprit.smarktgo.utils.ApiInterface
import com.esprit.smarktgo.utils.RetrofitInstance
import retrofit2.Response
import retrofit2.http.Body

class SupermarketRepository {

    val api = RetrofitInstance.getRetroInstance().create(ApiInterface::class.java)

    suspend fun getAll(): MutableList<Supermarket>?
    {
        val request =api.getAll()

        if (request.code()!=200) return null
        return request.body()
    }

}