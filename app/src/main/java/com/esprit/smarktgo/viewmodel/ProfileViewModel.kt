package com.esprit.smarktgo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.R
import com.esprit.smarktgo.model.ProfileItem
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.repository.UserRepository
import com.esprit.smarktgo.view.ProfileFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.launch


class ProfileFragmentViewModel(profileFragment: ProfileFragment) : ViewModel() {


    private val mFragment = profileFragment

    fun setList(profileList: MutableList<ProfileItem>) {
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


    fun updateProfile(fullname: String) {
        //  if (userId.contains("@")) {

        val user = User(userId, fullname, wallet = 0.0)

        viewModelScope.launch {

            val updateResult = userRepository.updateProfilee(user)
            if (updateResult == null) {
                Log.d("TAG", "NO RESULT")
            } else {

                Log.d("TAG", "$updateResult")
            }
        }
    }

    fun getUserInfo() {
        val user = User(userId, "", wallet = 0.0)

        viewModelScope.launch {
            val data = userRepository.signIn(user)
            if (data == null) {

                Log.d("TAG", "Tneket")
            } else {
                data.let {
                    userLiveData.value = data
                }
            }
        }
    }


    fun observeUser(): LiveData<User> = userLiveData


}