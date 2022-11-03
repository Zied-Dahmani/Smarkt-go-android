package com.esprit.smarktgo.viewmodel

import android.content.ContentValues
import android.util.Log
import com.esprit.smarktgo.view.SignInActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

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
                val personName = acct.displayName
                Log.d(ContentValues.TAG,"Value of a : $personName")
            }
        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
        }
    }


    public fun signOut() {
        mGoogleSignInClient.signOut()
    }
}