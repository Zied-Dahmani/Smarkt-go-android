package com.esprit.smarktgo.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.model.AddUser
import com.esprit.smarktgo.model.GetOrder
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.repository.OrderRepository
import com.esprit.smarktgo.repository.UserRepository
import com.esprit.smarktgo.view.CartGroupActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class CartGroupViewModel(mActivity: CartGroupActivity): ViewModel() {

    private val mActivity = mActivity
    lateinit var userId: String
    private lateinit var order : GetOrder
    private val orderRepository = OrderRepository()
    private val userRepository = UserRepository()
    private var usersLiveData = MutableLiveData<List<User>>()


    init {
        getOrder()
    }

    private fun getOrder() {
        try {

            val googleSignIn = GoogleSignIn.getLastSignedInAccount(mActivity)
            userId = if (googleSignIn != null) {
                googleSignIn.email!!
            } else FirebaseAuth.getInstance().currentUser?.phoneNumber!!

            viewModelScope.launch {
                val result = orderRepository.get(userId)

                result?.let {
                    order = result
                    getUsers()
                } ?: mActivity.showImage()

            }
        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }

    private fun getUsers() {
        try {
            viewModelScope.launch {
                val result = userRepository.getUsers(order)

                result?.let {
                    usersLiveData.value = result
                }
            }
        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }

    fun observeUsersLiveData() : LiveData<List<User>>  = usersLiveData

    fun addUser(id:String)
    {
        order.group.add(id)
        viewModelScope.launch {
            val result = orderRepository.addUser(AddUser(userId,order.group))
            result?.let {
            }
        }
    }
}