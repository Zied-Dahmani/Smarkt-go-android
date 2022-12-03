package com.esprit.smarktgo.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.esprit.smarktgo.R
import com.esprit.smarktgo.adapter.ProfileAdapter
import com.esprit.smarktgo.model.ProfileItem
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.model.updateUsername
import com.esprit.smarktgo.repository.OrderRepository
import com.esprit.smarktgo.repository.UserRepository
import com.esprit.smarktgo.view.ProfileFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch



class ProfileFragmentViewModel(profileFragment: ProfileFragment) : ViewModel() {


    private val mFragment = profileFragment

    fun setList(profileList: MutableList<ProfileItem>) {
        profileList.add(ProfileItem(mFragment.requireContext().getString(R.string.wallet), R.drawable.ic_baseline_wallet_24))
        profileList.add(ProfileItem(mFragment.requireContext().getString(R.string.cart_group), R.drawable.ic_baseline_shopping_cart_24))
        profileList.add(ProfileItem(mFragment.requireContext().getString(R.string.settings), R.drawable.ic_baseline_settings_24))
    }

    var profileList: MutableList<ProfileItem> = ArrayList()
    var userLiveData = MutableLiveData<User>()
    private val userRepository: UserRepository = UserRepository()
    private val orderRepository: OrderRepository = OrderRepository()
    var userId: String

    var orderId = ""

    init {
        val googleSignIn = GoogleSignIn.getLastSignedInAccount(mFragment.requireContext())
        userId = if(googleSignIn!=null) {
            googleSignIn.email!!
        } else FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()
        getUserInfo()
        getOrder()
    }
    fun validateData(fullname: String):Boolean
    {
        if (fullname.isEmpty())
        {
            mFragment.validateFullname(mFragment.getString(R.string.mustNotBeEmpty))
            return false
        }
        else {
         updateProfile(fullname)
        }
        return true
    }
    private fun updateProfile(fullname: String) {
        try {
        val user= updateUsername(userId,fullname)
        viewModelScope.launch {

            val updateResult = userRepository.updateUsername(user)
            if (updateResult == null) {
                Log.d("TAG", "NO RESULT")
            } else {
                Log.d("TAG", "$updateResult")
            }
        }
        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }

   private fun getUserInfo() {
        val user = User(userId, "", wallet = 0.0)
        try {
        viewModelScope.launch {
            val data = userRepository.signIn(user)
            data?.let {
                userLiveData.value = data
            }
        }
    } catch (e: ApiException) {
        Log.w(ContentValues.TAG, e.statusCode.toString())
    }
    }

     fun initRecyclerView() {
        setList(profileList)
        mFragment.profileAdapter = ProfileAdapter(profileList,null)
        mFragment.myRecycler.apply {
            adapter = mFragment.profileAdapter
           layoutManager = LinearLayoutManager(mFragment.view?.context, LinearLayoutManager.VERTICAL, false)
        }
    }


    fun observeUser(): LiveData<User> = userLiveData

    private fun getOrder() {
        try {
            viewModelScope.launch {
                val result = orderRepository.get(userId)

                result?.let {
                    orderId =  result.id
                }
            }
        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }

}