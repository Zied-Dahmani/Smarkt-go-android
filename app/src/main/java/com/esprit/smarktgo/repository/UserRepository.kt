package com.esprit.smarktgo.repository

import com.esprit.smarktgo.model.GetOrder
import com.esprit.smarktgo.model.Order
import com.esprit.smarktgo.model.Supermarket
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.utils.ApiInterface
import com.esprit.smarktgo.utils.RetrofitInstance
import retrofit2.Response
import retrofit2.http.Body

class UserRepository {

   val api = RetrofitInstance.getRetroInstance().create(ApiInterface::class.java)

   suspend fun signUp(@Body userRequest:User): Response<User>?
   {
      val request =api.signUp(userRequest)

      if (request.code()!=201) return null
      return request
   }

   suspend fun signIn(@Body userRequest: User):User?
   {
      val request =api.signIn(userRequest)

      if (request.code()!=200) return null
      return request.body()
   }

   suspend fun updateProfile(@Body user: User):User?
   {
      val request =api.updateProfile(user)

      if (request.code()!=200) return null
      return request.body()
   }

   suspend fun updateProfilee(@Body user: User):User?
   {
      val request =api.updateProfile(user)

      if (request.code()!=200) return null
      return request.body()
   }

   suspend fun getUsers(order: GetOrder): MutableList<User>?
   {
      val request =api.getUsers(order)

      if (request.code()!=200) return null
      return request.body()
   }

}