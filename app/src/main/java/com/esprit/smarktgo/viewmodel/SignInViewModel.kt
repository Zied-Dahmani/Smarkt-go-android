package com.esprit.smarktgo.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.utils.ApiInterface
import com.esprit.smarktgo.utils.RetrofitInstance
import com.esprit.smarktgo.view.SignInActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import retrofit2.Call
import retrofit2.Callback
import java.util.concurrent.TimeUnit

class SignInViewModel(signInActivity: SignInActivity): ViewModel() {

    var mGoogleSignInClient: GoogleSignInClient
    var gso: GoogleSignInOptions

    var mActivity = signInActivity

    var auth: FirebaseAuth
    var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken


    init{
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(signInActivity, gso)
        auth = FirebaseAuth.getInstance()

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("GFG", "onVerificationCompleted Success")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                mActivity.showSnackBar("Try later!")
                Log.d("GFG", "onVerificationFailed  $e")
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken)
            {
                Log.d("GFG","onCodeSent: $verificationId")
                storedVerificationId = verificationId
                resendToken = token
                mActivity.navigateToOtpActivity(storedVerificationId)
            }
        }
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>){

        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)

            val retroService = RetrofitInstance.getRetroInstance().create(ApiInterface::class.java)

            val call = retroService.signIn(account.email.toString())

            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: retrofit2.Response<User>) {

                    if (response.body()!=null) {
                        mActivity.navigateToMainActivity(true)
                    }
                    else
                    {
                        val user = User(id = account.email.toString(), fullName = account.displayName.toString(), wallet = 0F)
                        val call2 = retroService.signUp(user)
                        call2.enqueue(object : Callback<User> {
                            override fun onResponse(call: Call<User>, response: retrofit2.Response<User>) {
                                if (response.isSuccessful) {
                                    mActivity.navigateToMainActivity(true)
                                }
                            }
                            override fun onFailure(call: Call<User>, t: Throwable) {
                                Log.w(ContentValues.TAG, "Sign Up onFailure")
                                mActivity.navigateToMainActivity(false)
                            }
                        })
                    }
                }
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.w(ContentValues.TAG, "Sign In onFailure")
                    mActivity.navigateToMainActivity(false)
                }
            })

        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
            mActivity.navigateToMainActivity(false)
        }
    }

    fun signInWithPhoneNumber(number: String)  : Boolean {
        if(number.isEmpty())
        {
            mActivity.showError("Type your phone number!")
            return false
        }
        else if(number.length!=8)
        {
            mActivity.showError("Type a valid phone number!")
            return false
        }
        else
            sendVerificationCode("+216$number")
        return true
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(mActivity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        Log.d("GFG" , "Auth started")
    }


}