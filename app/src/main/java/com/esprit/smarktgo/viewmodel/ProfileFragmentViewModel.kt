package com.esprit.smarktgo.viewmodel

import androidx.lifecycle.ViewModel
import com.esprit.smarktgo.R
import com.esprit.smarktgo.adapter.ProfileAdapter
import com.esprit.smarktgo.model.ProfileItem

class ProfileFragmentViewModel :ViewModel() {

   lateinit var  profileAdapter:ProfileAdapter



    fun setList( profileList : MutableList<ProfileItem>)
    {
         profileList.add(ProfileItem("Profile", R.drawable.ic_baseline_person_24))
        profileList.add(ProfileItem("Settings",R.drawable.ic_baseline_settings_24))
        profileList.add(ProfileItem("Log Out",R.drawable.ic_baseline_logout_24))
    }

}