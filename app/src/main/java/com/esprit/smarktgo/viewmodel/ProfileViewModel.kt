package com.esprit.smarktgo.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.repository.UserRepository
import com.esprit.smarktgo.view.ProfileActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


val phoneAccount = FirebaseAuth.getInstance().currentUser


class ProfileViewModel(mActivity: ProfileActivity, fullname: String) : ViewModel() {
    var userRepository: UserRepository
    val mActivity = ProfileActivity()
    lateinit var userId: String
    var fn = fullname

    init {
        userRepository = UserRepository()
        GoogleSignIn.getLastSignedInAccount(mActivity).let {
            userId = it?.email!!
        }

    }

    fun updateProfile() {
      //  if (userId.contains("@")) {

            val user = User(userId, fn, wallet = 0F)

            viewModelScope.launch {
                val updateResult = userRepository.updateProfile(user)
                if (updateResult == null) {
                    Log.d("TAG", "Ma taada chay")
                } else {
                    Log.d("TAG", "$updateResult")
                }
            }
        }

    //}



}