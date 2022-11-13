package com.esprit.smarktgo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.esprit.smarktgo.R
import com.esprit.smarktgo.adapter.CategoryAdapter
import com.esprit.smarktgo.adapter.ItemAdapter
import com.esprit.smarktgo.databinding.ActivityItemsBinding
import com.esprit.smarktgo.databinding.ActivitySupermarketBinding
import com.esprit.smarktgo.viewmodel.ItemsViewModel
import com.esprit.smarktgo.viewmodel.SupermarketViewModel

class ItemsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityItemsBinding
    private lateinit var itemViewModel: ItemsViewModel
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)
        binding = ActivityItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        val category= intent.getStringExtra("category")
        val supermarketId= intent.getStringExtra("supermarketId")
        prepareRecyclerView()
        itemViewModel = ItemsViewModel(this)
        itemViewModel.getItems(category!!, supermarketId!!)

        itemViewModel.observeItemsLiveData().observe(this, Observer { list ->
            itemAdapter.setList(list)
        })
    }

    private fun prepareRecyclerView() {
        itemAdapter = ItemAdapter()
        binding.rvItems.apply {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)
        }
    }

    fun showImage(){
        binding.imageView.isVisible = true
        binding.noItemsTV.isVisible = true
    }




}