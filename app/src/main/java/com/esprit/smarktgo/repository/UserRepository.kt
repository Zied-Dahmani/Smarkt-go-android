package com.esprit.smarktgo.repository

import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.utils.ApiInterface
import com.esprit.smarktgo.utils.RetrofitInstance
import com.google.android.gms.common.api.Api
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path

class UserRepository {
   val api = RetrofitInstance.getRetroInstance().create(ApiInterface::class.java)

suspend fun signUp(@Body userRequest:User): Response<User>?
{
   val request =api.signUp(userRequest)

if (!request.isSuccessful)
{
   return null
}

else

   return request

}
suspend fun signIn(@Body userRequest: User):User?
{
   val request =api.signIn(userRequest)
   if (!request.isSuccessful)
   {
   return null
   }

   return request.body()
}

}