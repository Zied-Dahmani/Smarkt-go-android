package com.esprit.smarktgo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.esprit.smarktgo.R
import com.esprit.smarktgo.databinding.ActivityMainBinding
import com.esprit.smarktgo.databinding.ActivitySupermarketBinding

private lateinit var binding : ActivitySupermarketBinding

class Supermarket : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supermarket)
        binding = ActivitySupermarketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent

        val name = intent.getStringExtra("name")
        val description= intent.getStringExtra("description")
        val address= intent.getStringExtra("address")
        val image= intent.getStringExtra("image")

        Glide.with(applicationContext).load("http://192.168.1.4:9090/img/" + image).into(binding.MarketImage)
        binding.MarketDescription.text = description
        binding.MarketName.text = name
        binding.MarketAddress.text=address


    }
}