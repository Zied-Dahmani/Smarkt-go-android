package com.esprit.smarktgo.view

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.esprit.smarktgo.R
import com.esprit.smarktgo.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignInActivity : AppCompatActivity() {

    companion object{
        const val RC_SIGN_IN = 1001
        const val EXTRA_NAME = "EXTRA_NAME"

    }

    private lateinit var binding : ActivitySignInBinding

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var auth: FirebaseAuth
    lateinit var gso: GoogleSignInOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding = ActivitySignInBinding.inflate(layoutInflater)

        FirebaseApp.initializeApp(this);
        auth = Firebase.auth

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("357498047821-cpn5nu9ajbibf9u8aosohumpiivc9qjp.apps.googleusercontent.com")
            .requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


         binding.googleButton.setOnClickListener { signIn() }
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                handleSignInResult(account.idToken!!)
            } catch (e: ApiException){
                Log.w(TAG, "Google sign in failed")
            }
        }
    }

    private fun handleSignInResult(idToken: String) {         
        val credential = GoogleAuthProvider.getCredential(idToken,null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this){task ->
                    if(task.isSuccessful){
                        val user = auth.currentUser
                        Log.w(TAG, user?.displayName.toString())
                    }
                    else
                        Log.w(TAG, "SignWithCredential failed")
                }
    }


}