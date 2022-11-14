package com.esprit.smarktgo.view

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.esprit.smarktgo.R
import com.esprit.smarktgo.viewmodel.ProfileViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.textfield.TextInputEditText

class ProfileActivity : AppCompatActivity() {


    private var txtFullName: TextInputEditText? = null
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        txtFullName = findViewById(R.id.profileFullname)

        val save = findViewById<Button>(R.id.editButton)
        val image = findViewById<ImageView>(R.id.profileImage)
        val wallet = findViewById<TextView>(R.id.profileWallet)
        val googleAccount = GoogleSignIn.getLastSignedInAccount(this)
        val imageURL = googleAccount?.photoUrl
            Glide.with(this)
                .load(imageURL)
                .circleCrop()
                .into(image)


        save.setOnClickListener {
            val input = txtFullName!!.text.toString()
            profileViewModel = ProfileViewModel(this, input)
            profileViewModel.updateProfile()
        }


    }
}