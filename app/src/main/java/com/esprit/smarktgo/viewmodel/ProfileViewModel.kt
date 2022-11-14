package com.esprit.smarktgo.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.repository.UserRepository
import com.esprit.smarktgo.utils.ApiInterface
import com.esprit.smarktgo.utils.RetrofitInstance
import com.esprit.smarktgo.view.ProfileActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import retrofit2.Callback

import retrofit2.Call
import retrofit2.Response


val phoneAccount = FirebaseAuth.getInstance().currentUser


class ProfileViewModel(mActivity: ProfileActivity, fullname: String) : ViewModel() {
    var userLiveData = MutableLiveData<User>()

    var userRepository: UserRepository
    val mActivity = ProfileActivity()
    lateinit var userId: String
    var fn = fullname

    init {
        userRepository = UserRepository()
        GoogleSignIn.getLastSignedInAccount(mActivity).let {
            userId = it?.email!!
getUserInfo()
        }


    }




    fun updateProfile() {
        //  if (userId.contains("@")) {

        val user = User(userId, fn, wallet = 0F)

        viewModelScope.launch {

            val updateResult = userRepository.updateProfile(user)
            if (updateResult == null) {
                Log.d("TAG", "NO RESULT")
            } else {

                Log.d("TAG", "$updateResult")
            }
        }
    }

    //}
    fun getUserInfo(): User {
        val retroService = RetrofitInstance.getRetroInstance().create(ApiInterface::class.java)
        val user = User(userId, fn, wallet = 0F)
        var data = User("", "", 0F)

        retroService.getInfo(user)

        val call = retroService.getInfo(user)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    data = response.body()!!
                    data.let {
                        userLiveData.value = data
                    }
                    Log.d("TF","$data")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
           }
        })

        return data
    }

    fun observeUser() : LiveData<User> = userLiveData


}




