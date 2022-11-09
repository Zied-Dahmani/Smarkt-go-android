package com.esprit.smarktgo.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.esprit.smarktgo.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val googleAccount = GoogleSignIn.getLastSignedInAccount(this)
        val phoneAccount = FirebaseAuth.getInstance().currentUser

        handler = Handler()
        handler.postDelayed({
            if (phoneAccount!=null|| googleAccount?.email !=null)
            {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else{
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
            finish()
        },3000)
    }

}



