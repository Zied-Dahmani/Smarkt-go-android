package com.esprit.smarktgo.view


import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esprit.smarktgo.R
import com.esprit.smarktgo.SupermarketAdapter
import com.esprit.smarktgo.databinding.FragmentHomeBinding
import com.esprit.smarktgo.model.Location
import com.esprit.smarktgo.model.Supermarket
import com.esprit.smarktgo.viewmodel.HomeViewModel


class HomeFragment : Fragment() {

    lateinit var homeViewModel: HomeViewModel
    lateinit var rv: RecyclerView

    private lateinit var supermarketAdapter : SupermarketAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //binding = FragmentHomeBinding.inflate(layoutInflater)
        val view = inflater.inflate(R.layout.fragment_home,container,false)

        rv = view.findViewById<RecyclerView>(R.id.rv_supermarkets)

        homeViewModel = HomeViewModel(this)

        prepareRecyclerView()
        homeViewModel.getAll()

        homeViewModel.observeSupermarketsLiveData().observe(requireActivity(), Observer { list ->
            supermarketAdapter.setList(list)
            Log.e(ContentValues.TAG, (list).size.toString())
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