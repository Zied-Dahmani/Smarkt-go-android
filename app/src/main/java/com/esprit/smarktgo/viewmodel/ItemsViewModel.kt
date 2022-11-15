package com.esprit.smarktgo.viewmodel

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.model.Item
import com.esprit.smarktgo.model.Order
import com.esprit.smarktgo.repository.ItemRepository
import com.esprit.smarktgo.repository.OrderRepository
import com.esprit.smarktgo.view.ItemsActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ItemsViewModel(itemsActivity: ItemsActivity): ViewModel()  {

    var itemsLiveData = MutableLiveData<List<Item>>()
    @SuppressLint("StaticFieldLeak")
    private val mActivity = itemsActivity
    lateinit var userId: String


    fun getItems(category:String,supermarketId:String) {
        try {
            val itemRepository = ItemRepository()

            viewModelScope.launch {
                val result = itemRepository.getAllBySupermarketIdAndCategory(category,supermarketId)
                result.let {
                    itemsLiveData.value = result
                }
                if(result!!.isEmpty())
                    mActivity.showImage()
            }

        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }

    fun observeItemsLiveData() : LiveData<List<Item>> = itemsLiveData

    fun addToCart(item: Item, quantity : Int) {
        try {
            val googleSignIn = GoogleSignIn.getLastSignedInAccount(mActivity.baseContext)
            userId = if(googleSignIn!=null) {
                googleSignIn.email!!
            } else FirebaseAuth.getInstance().currentUser?.phoneNumber!!


            val orderRepository = OrderRepository()
            var itemsList = ArrayList<Item>()
            item.quantity = quantity
            itemsList.add(item)
            val order = Order(userId,itemsList)

            viewModelScope.launch {
                val result = orderRepository.addToCart(order)
                if(result!=null&&result.code()!=400)
                    mActivity.showSuccessSnackBar("Added to cart")
                else if (result?.code() == 400)
                    mActivity.showSuccessSnackBar("You already have orders from another supermarket!")

            }
        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }


}