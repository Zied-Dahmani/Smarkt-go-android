package com.esprit.smarktgo.viewmodel

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.utils.ApiInterface
import com.esprit.smarktgo.utils.ApiManager
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

class SignInViewModel(signInActivity: SignInActivity) {

     var mGoogleSignInClient: GoogleSignInClient
     var gso: GoogleSignInOptions
     var  x = signInActivity

    init{
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(signInActivity, gso);
    }

    public fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {

            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            val acct = GoogleSignIn.getLastSignedInAccount(x)
            if (acct != null) {


                val user = User(
                    id = acct.email.toString(),
                    fullName = acct.displayName.toString(),
                    wallet = 0F
                )
                val retroService =
                    RetrofitInstance.getRetroInstance().create(ApiInterface::class.java)
                val call = retroService.signUp(user)
                call.enqueue(object : Callback<User> {
                    override fun onFailure(call: Call<User>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<User>, response: retrofit2.Response<User>) {
                        if (response.isSuccessful) {
                            retroService.signUp(user)
                        }
                    }
                })


            }
        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
        }
    }


    public fun signOut() {
        mGoogleSignInClient.signOut()
    }
}