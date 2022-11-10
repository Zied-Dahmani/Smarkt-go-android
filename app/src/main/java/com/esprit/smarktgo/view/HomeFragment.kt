package com.esprit.smarktgo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esprit.smarktgo.R
import com.esprit.smarktgo.adapter.SupermarketAdapter
import com.esprit.smarktgo.viewmodel.HomeViewModel


class HomeFragment : Fragment() {

    lateinit var homeViewModel: HomeViewModel
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
        supermarketAdapter = SupermarketAdapter()
        rv.apply {
            adapter = supermarketAdapter
            layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL ,false)
        }
    }

}