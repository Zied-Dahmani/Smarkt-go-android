package com.esprit.smarktgo.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.esprit.smarktgo.R
import com.esprit.smarktgo.databinding.ActivitySignInBinding
import com.esprit.smarktgo.viewmodel.SignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task


const val RC_SIGN_IN = 1001

class SignInActivity : AppCompatActivity() {

    lateinit var signInViewModel : SignInViewModel
    private lateinit var binding : ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signInViewModel= SignInViewModel(this)

        binding.googleButton.setOnClickListener {
        signIn()
        }

        binding.signOut.setOnClickListener {
            signInViewModel.signOut()
        }
    }

    private fun signIn() {
        val signInIntent = signInViewModel.mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            signInViewModel.handleSignInResult(task)
        }
    }

}