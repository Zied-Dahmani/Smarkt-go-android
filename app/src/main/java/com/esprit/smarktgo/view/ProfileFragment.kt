package com.esprit.smarktgo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esprit.smarktgo.R
import com.esprit.smarktgo.adapter.ProfileAdapter
import com.esprit.smarktgo.model.ProfileItem
import com.esprit.smarktgo.viewmodel.ProfileFragmentViewModel


class ProfileFragment: Fragment() {

    private lateinit var profileViewModel: ProfileFragmentViewModel
    lateinit var myRecycler: RecyclerView
    private lateinit var profileAdapter : ProfileAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile,container,false)
        myRecycler = view.findViewById(R.id.profileRecycler)
        profileViewModel = ProfileFragmentViewModel()

initRecyclerView()



        return view
    }
    private fun initRecyclerView() {
       var profileList : MutableList<ProfileItem> =ArrayList()
        profileViewModel.setList(profileList)
        profileAdapter = ProfileAdapter(profileList)
        myRecycler.apply {
            adapter = profileAdapter
            layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL ,false)
        }
    }



}