package com.esprit.smarktgo.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esprit.smarktgo.R
import com.esprit.smarktgo.adapter.SupermarketAdapter
import com.esprit.smarktgo.model.Location
import com.esprit.smarktgo.viewmodel.HomeViewModel


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var rv: RecyclerView
    private lateinit var supermarketAdapter : SupermarketAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_home,container,false)
        rv = view.findViewById(R.id.rv_supermarkets)

        prepareRecyclerView()
        homeViewModel = HomeViewModel(this)
        homeViewModel.observeSupermarketsLiveData().observe(requireActivity(), Observer { list ->
            supermarketAdapter.setList(list)
        })
        return view
    }

    private fun prepareRecyclerView() {
        supermarketAdapter = SupermarketAdapter(this@HomeFragment)
        rv.apply {
            adapter = supermarketAdapter
            layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL ,false)
        }
    }

    fun navigateToSupermarketActivity(name: String, description: String?, address: String?, image: String?,location: Location) {
        val intent = Intent(requireContext(), SupermarketActivity::class.java).apply {
            putExtra("name", name)
            putExtra("description", description)
            putExtra("address", address)
            putExtra("image", image)
            putExtra("latitude", location.coordinates[0])
            putExtra("longitude", location.coordinates[1])
        }
        startActivity(intent)
    }




}