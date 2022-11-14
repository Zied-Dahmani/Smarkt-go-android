package com.esprit.smarktgo.view

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.esprit.smarktgo.R
import com.esprit.smarktgo.viewmodel.ProfileViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ProfileActivity : AppCompatActivity() {

 lateinit var fullNameLayout:TextInputLayout
    lateinit var txtFullName: TextInputEditText
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        txtFullName = findViewById(R.id.profileFullname)
        fullNameLayout=findViewById(R.id.fullnameLayout)

        val save = findViewById<Button>(R.id.editButton)
        val image = findViewById<ImageView>(R.id.profileImage)
        val wallet = findViewById<TextView>(R.id.profileWallet)
        val name = findViewById<TextView>(R.id.name)
        val googleAccount = GoogleSignIn.getLastSignedInAccount(this)
        val imageURL = googleAccount?.photoUrl
            Glide.with(this)
                .load(imageURL)
                .circleCrop()
                .into(image)
        profileViewModel=ProfileViewModel(this,"")

        profileViewModel.observeUser().observe(this, Observer {
            val u=profileViewModel.userLiveData.value
            var walletV:CharSequence=(""+u?.wallet)
            var nameV:CharSequence=(""+u?.fullName)
            wallet?.text=walletV
            name?.text=nameV

        })



        save.setOnClickListener {
            val input = txtFullName!!.text.toString()
            if (validate()) {
                profileViewModel = ProfileViewModel(this, input)
                profileViewModel.updateProfile()
            }}



    }
    fun validate ():Boolean{
        fullNameLayout.error=null
        if (txtFullName.text!!.isEmpty()){
            fullNameLayout.error = getString(R.string.mustNotBeEmpty)
            return false
        }
        return true
    }
}