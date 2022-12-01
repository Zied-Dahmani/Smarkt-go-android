package com.esprit.smarktgo.viewmodel

import android.content.ContentValues
import android.content.res.Configuration
import android.content.res.Resources
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
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.util.Locale


class ProfileFragmentViewModel(profileFragment: ProfileFragment) : ViewModel() {


    private val mFragment = profileFragment

    fun setList(profileList: MutableList<ProfileItem>) {
        profileList.add(ProfileItem(mFragment.requireContext().getString(R.string.wallet), R.drawable.ic_baseline_wallet_24))
        profileList.add(ProfileItem(mFragment.requireContext().getString(R.string.cart_group), R.drawable.ic_baseline_shopping_cart_24))
        profileList.add(ProfileItem(mFragment.requireContext().getString(R.string.settings), R.drawable.ic_baseline_settings_24))
    }

    var userLiveData = MutableLiveData<User>()
    var userRepository: UserRepository
    var userId: String

    init {
        userRepository = UserRepository()
        val googleSignIn = GoogleSignIn.getLastSignedInAccount(mFragment.requireContext())
        userId = if(googleSignIn!=null) {
            googleSignIn.email!!
        } else FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()
        getUserInfo()

    }

    fun updateProfile(fullname: String) {
        //  if (userId.contains("@")) {
        try {
        val user = User(userId, fullname, wallet = 0.0)

        viewModelScope.launch {

            val updateResult = userRepository.updateProfilee(user)
            if (updateResult == null) {
                Log.d("TAG", "NO RESULT")
            } else {

                Log.d("TAG", "$updateResult")
            }
        }
        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }

    fun getUserInfo() {
        val user = User(userId, "", wallet = 0.0)
        try {

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
    } catch (e: ApiException) {
        Log.w(ContentValues.TAG, e.statusCode.toString())
    }
    }


    fun observeUser(): LiveData<User> = userLiveData


}