package com.esprit.smarktgo.viewmodel

import android.content.ContentValues
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.model.Order
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.repository.OrderRepository
import com.esprit.smarktgo.repository.UserRepository
import com.esprit.smarktgo.view.CartFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class CartViewModel(cartFragment: CartFragment): ViewModel() {

    private val mFragment = cartFragment
    lateinit var userId: String
    private var orderLiveData = MutableLiveData<Order>()
    private val orderRepository = OrderRepository()
    private var wallet = 0.0
    val userRepository = UserRepository()
    lateinit var user : User


    init {
        getOrder()
    }

    private fun getOrder() {
        try {

            val googleSignIn = GoogleSignIn.getLastSignedInAccount(mFragment.requireContext())
            userId = if(googleSignIn!=null) {
                googleSignIn.email!!
            } else FirebaseAuth.getInstance().currentUser?.phoneNumber!!

            viewModelScope.launch {
                val result = orderRepository.get(userId)

                result?.let {
                    orderLiveData.value = result
                    mFragment.showOrderInfo(true)
                }?: mFragment.showImage()


            }
        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }

    fun observeOrderLiveData() : LiveData<Order> = orderLiveData

    fun getTotal() : Double
    {
        var total = 0.0
        for (item in orderLiveData.value?.items!!)
            total+= item.quantity * item.price
        return total
    }

    fun removeItem(index: Int)
    {
        viewModelScope.launch {
            orderLiveData.value!!.items.removeAt(index)
            val result = orderRepository.removeItem(orderLiveData.value!!)

            result.let {
                mFragment.itemAdapter.setList(orderLiveData.value!!.items)
                mFragment.priceTV.text = "%.1f".format(getTotal()) + " TND"
                if (orderLiveData.value!!.items.isEmpty())
                    deleteOrder()
            }
        }
    }

    private fun deleteOrder()
    {
        viewModelScope.launch {
            val result = orderRepository.deleteOrder(User(orderLiveData.value!!.userId,"",0.0))
            result.let {
                mFragment.showOrderInfo(false)
                mFragment.showImage()
            }
        }
    }

    fun showWallet(walletTV : TextView)
    {
        viewModelScope.launch {
            val result = userRepository.signIn(User(userId,"",0.0))
            result?.let {
                user = it
                wallet = it.wallet
                walletTV.text =  "%.1f".format(it.wallet) + " TND"
            }
        }
    }

    fun pay()
    {
        val total = getTotal()
        if(wallet>=total)
        {
            updateWallet(wallet-total)
            deleteOrder()
            mFragment.bottomSheetDialog.dismiss()
        }
        else
            mFragment.showSnackBar()
    }

    private fun updateWallet(walletParam : Double)
    {
        viewModelScope.launch {
          userRepository.updateProfile(User(user.id,user.fullName,walletParam))
        }
    }


}