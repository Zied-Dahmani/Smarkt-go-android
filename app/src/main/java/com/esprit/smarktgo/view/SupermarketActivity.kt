package com.esprit.smarktgo.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.esprit.smarktgo.R
import com.esprit.smarktgo.adapter.CategoryAdapter
import com.esprit.smarktgo.databinding.ActivitySupermarketBinding
import com.esprit.smarktgo.viewmodel.SupermarketViewModel


class SupermarketActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySupermarketBinding
    private lateinit var supermarketViewModel: SupermarketViewModel
    private lateinit var categoryAdapter: CategoryAdapter

    var latitude: Double = 0.0
    var longitude: Double = 0.0
    lateinit var name: String
    lateinit var supermarketId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supermarket)
        binding = ActivitySupermarketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        name = intent.getStringExtra("name").toString()
        supermarketId = intent.getStringExtra("supermarketId").toString()
        latitude = intent.getDoubleExtra("latitude",0.0)
        longitude = intent.getDoubleExtra("longitude",0.0)
        val description= intent.getStringExtra("description")
        val address= intent.getStringExtra("address")
        val image= intent.getStringExtra("image")
        Glide.with(applicationContext).load("http://192.168.1.4:9090/img/" + image).into(binding.supermarketImage)
        binding.supermarketName.text = name
        binding.supermarketAddress.text=address
        binding.supermarketDescription.text=description

        prepareRecyclerView()
        supermarketViewModel = SupermarketViewModel(this,supermarketId)
        supermarketViewModel.observeCategoriesLiveData().observe(this, Observer { list ->
            categoryAdapter.setList(list)
        })

        supermarketViewModel.observeIsFavoriteLiveData().observe(this, Observer { isFavorite ->
            if(isFavorite)
                binding.favorite.setImageResource(R.drawable.ic_baseline_favorite_filled_24)
            else
                binding.favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)

        })

        binding.favorite.setOnClickListener {
            if(supermarketViewModel.isFavorite.value!!)
            supermarketViewModel.removeFavorite()
            else
                supermarketViewModel.addFavorite()
        }

        binding.marker2.setOnClickListener{
            launchGoogleMaps()
        }
    }

    private fun prepareRecyclerView() {
        categoryAdapter = CategoryAdapter(this)
        binding.rvCategories.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)
        }
    }

    fun navigateToItemsActivity(category:String){
        val intent = Intent(this, ItemsActivity::class.java).apply {
            putExtra("category", category)
            putExtra("supermarketId",supermarketId )
        }
        startActivity(intent)
    }

    private fun launchGoogleMaps()
    {
        val supermarketLatitude = latitude
        val supermarketLongitude = longitude
        val labelLocation = name
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:<$supermarketLatitude>,<$supermarketLongitude>?q=<$supermarketLatitude>,<$supermarketLongitude>($labelLocation)"))
        startActivity(intent)
    }


}