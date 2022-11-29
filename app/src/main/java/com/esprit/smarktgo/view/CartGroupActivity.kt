package com.esprit.smarktgo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.esprit.smarktgo.R
import com.esprit.smarktgo.adapter.UserAdapter
import com.esprit.smarktgo.databinding.ActivityCartGroupBinding
import com.esprit.smarktgo.viewmodel.CartGroupViewModel
import com.google.android.material.snackbar.Snackbar

class CartGroupActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCartGroupBinding
    private lateinit var cartGroupViewModel: CartGroupViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_group)
        binding = ActivityCartGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener { finish() }

        cartGroupViewModel = CartGroupViewModel(this)
        prepareRecyclerView()
        cartGroupViewModel.observeUsersLiveData().observe(this, Observer { users ->
            userAdapter.setList(users)
        })

    }

    private fun prepareRecyclerView() {
        userAdapter = UserAdapter(this)
        binding.rvUsers.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL ,false)
        }
    }

    fun showImage(){
        binding.imageView.isVisible = true
        binding.emptyCartTV.isVisible = true
    }

    fun addUser(userId: String)
    {
        cartGroupViewModel.addUser(userId)
    }

    fun showSnackBar(text: String)
    {
        Snackbar.make(findViewById(R.id.cartGroupConstraintLayout),text, Snackbar.LENGTH_LONG).show()
    }
}