package com.esprit.smarktgo.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.esprit.smarktgo.R
import com.esprit.smarktgo.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    val favoritesFragment = FavoritesFragment()

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

        replaceFragment(HomeFragment())

        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId)
            {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.cart -> replaceFragment(CartFragment())
                R.id.favorites -> replaceFragment(favoritesFragment)
                else -> true
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment){
        val bundle = Bundle()
        fragment.arguments = bundle
        val fragmentManager =  supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
    }

    override fun onResume() {
        super.onResume()
        if(binding.bottomNav.selectedItemId == R.id.favorites)
            favoritesFragment.updateList()
    }
}