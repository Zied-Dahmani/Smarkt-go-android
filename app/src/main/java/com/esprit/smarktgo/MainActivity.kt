package com.esprit.smarktgo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.esprit.smarktgo.databinding.ActivityMainBinding
import com.esprit.smarktgo.view.SignInActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.signOutButton.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)

            mGoogleSignInClient.signOut()
            FirebaseAuth.getInstance().signOut()
        }

    }
}