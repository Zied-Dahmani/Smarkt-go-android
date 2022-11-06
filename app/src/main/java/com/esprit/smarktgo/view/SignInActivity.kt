package com.esprit.smarktgo.view

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.esprit.smarktgo.MainActivity
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
    val loading = LoadingDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signInViewModel= SignInViewModel(this)

        binding.googleButton.setOnClickListener {
            signIn()
        }

        binding.phoneButton.setOnClickListener { signInViewModel.signOut() }

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
            loading.startLoading()
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            signInViewModel.handleSignInResult(task)
        }
    }

    fun navigate(result:Boolean) {
        loading.dismiss()
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        if (result) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else
            Toast.makeText(applicationContext, "Failed!", Toast.LENGTH_LONG).show()
    }

}