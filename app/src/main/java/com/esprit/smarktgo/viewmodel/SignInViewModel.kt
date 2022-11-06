package com.esprit.smarktgo.viewmodel

import android.content.ContentValues
import android.util.Log
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.utils.ApiInterface
import com.esprit.smarktgo.utils.RetrofitInstance
import com.esprit.smarktgo.view.SignInActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Response
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit

class SignInViewModel(signInActivity: SignInActivity) {

     var mGoogleSignInClient: GoogleSignInClient
     var gso: GoogleSignInOptions
     var mActivity = signInActivity

    init{
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(signInActivity, gso);
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>){

        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            //val acct = GoogleSignIn.getLastSignedInAccount(x)

            val retroService = RetrofitInstance.getRetroInstance().create(ApiInterface::class.java)

            val call = retroService.signIn(account.email.toString())

            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: retrofit2.Response<User>) {

                    if (response.body()!=null) {
                        mActivity.navigate(true)
                    }
                    else
                    {
                        val user = User(id = account.email.toString(), fullName = account.displayName.toString(), wallet = 0F)
                        val call2 = retroService.signUp(user)
                        call2.enqueue(object : Callback<User> {
                            override fun onResponse(call: Call<User>, response: retrofit2.Response<User>) {
                                if (response.isSuccessful) {
                                    mActivity.navigate(true)
                                }
                            }
                            override fun onFailure(call: Call<User>, t: Throwable) {
                                Log.w(ContentValues.TAG, "Sign Up onFailure")
                                mActivity.navigate(false)
                            }
                        })
                    }
                }
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.w(ContentValues.TAG, "Sign In onFailure")
                    mActivity.navigate(false)
                }
            })

        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
            mActivity.navigate(false)

        }
    }


    fun signOut() {
        mGoogleSignInClient.signOut()
    }

}