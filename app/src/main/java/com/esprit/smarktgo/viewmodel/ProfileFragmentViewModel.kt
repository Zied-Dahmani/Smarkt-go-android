package com.esprit.smarktgo.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.R
import com.esprit.smarktgo.adapter.ProfileAdapter
import com.esprit.smarktgo.model.ProfileItem
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.repository.UserRepository
import com.esprit.smarktgo.utils.ApiInterface
import com.esprit.smarktgo.utils.RetrofitInstance
import com.esprit.smarktgo.view.ProfileActivity
import com.esprit.smarktgo.view.ProfileFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragmentViewModel(profileFragment: ProfileFragment) :ViewModel() {

    lateinit var profileAdapter: ProfileAdapter

    private val mFragment = profileFragment

    fun setList(profileList: MutableList<ProfileItem>) {

        profileList.add(ProfileItem("Profile", R.drawable.ic_baseline_person_24))
        profileList.add(ProfileItem("Settings", R.drawable.ic_baseline_settings_24))
        profileList.add(ProfileItem("Log Out", R.drawable.ic_baseline_logout_24))
    }

    var userLiveData = MutableLiveData<User>()

    var userRepository: UserRepository
    val mActivity = ProfileFragment()
    lateinit var userId: String

    init {
        userRepository = UserRepository()
        GoogleSignIn.getLastSignedInAccount(mFragment.requireContext()).let {
            userId = it?.email!!
            getUserInfo()
        }


    }


    fun updateProfile(fullname:String) {
        //  if (userId.contains("@")) {

             val user = User(userId, fullname, wallet = 0F)

           viewModelScope.launch {

            val updateResult = userRepository.updateProfile(user)
            if (updateResult == null) {
                Log.d("TAG", "NO RESULT")
            } else {

                Log.d("TAG", "$updateResult")
            }
        }
    }

    fun getUserInfo(){
            val user = User(userId, "", wallet = 0F)

            viewModelScope.launch {
                val data = userRepository.signIn(user)
                if (data == null) {

                    Log.d("TAG", "Tneket")
                } else {
                    data.let {
                        userLiveData.value = data
                    }
                }
            }}




        fun observeUser(): LiveData<User> = userLiveData







    fun getUserInfo2(): User {
        val retroService = RetrofitInstance.getRetroInstance().create(ApiInterface::class.java)
        val user = User(userId, "", wallet = 0F)
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

    }
