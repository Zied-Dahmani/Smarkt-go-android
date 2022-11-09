package com.esprit.smarktgo.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.repository.UserRepository
import com.esprit.smarktgo.utils.ApiInterface
import com.esprit.smarktgo.utils.RetrofitInstance
import com.esprit.smarktgo.view.OtpActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

class OtpViewModel( otpActivity: OtpActivity): ViewModel() {

    var auth: FirebaseAuth
    val mActivity = otpActivity
    var userRepository: UserRepository

    init {
        userRepository = UserRepository()
        auth = FirebaseAuth.getInstance()
    }

    fun verifyOTP(otp: String, storedVerificationId: String) {
        if (otp.isEmpty())
            mActivity.showError("Type the OTP!")
        else if (otp.length != 6)
            mActivity.showError("Tye a 6-digit code!")
        else {
            val credential: PhoneAuthCredential =
                PhoneAuthProvider.getCredential(storedVerificationId, otp)
            signInWithPhoneAuthCredential(credential)
        }
    }

    // verifies if the code matches sent by firebase
    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(mActivity) { task ->
                if (task.isSuccessful) {
                    handleSignInResult()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        mActivity.showError("Invalid OTP")
                    }
                }
            }
    }

    fun handleSignInResult() {

        try {
            val account = auth.currentUser
            val retroService = RetrofitInstance.getRetroInstance().create(ApiInterface::class.java)

            val usertoCheck = User(account?.phoneNumber.toString(), "", 0F)
            viewModelScope.launch {
                val signinResult = userRepository.signIn(usertoCheck)
                if (signinResult == null) {
                    val user =
                        User(id = account?.phoneNumber.toString(), fullName = "", wallet = 0F)
                    val myTry = userRepository.signUp(user)
                    mActivity.navigate(true)
                } else {
                    mActivity.navigate(true)

                }

            }

        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
            mActivity.navigate(false)
        }
    }

}
