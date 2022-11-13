package com.esprit.smarktgo.viewmodel

import androidx.lifecycle.ViewModel
import com.esprit.smarktgo.adapter.ProfileAdapter
import com.esprit.smarktgo.model.ProfileItem

class ProfileViewModel :ViewModel() {

   lateinit var  profileAdapter:ProfileAdapter



    fun setList( profileList : MutableList<ProfileItem>)
    {
         profileList.add(ProfileItem("Profile","profile"))
        profileList.add(ProfileItem("Settings","settings"))
        profileList.add(ProfileItem("Log Out","logout"))
    }

}