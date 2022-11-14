package com.esprit.smarktgo.viewmodel

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.model.Item
import com.esprit.smarktgo.repository.ItemRepository
import com.esprit.smarktgo.view.ItemsActivity
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

class ItemsViewModel(itemsActivity: ItemsActivity): ViewModel()  {

    var itemsLiveData = MutableLiveData<List<Item>>()
    @SuppressLint("StaticFieldLeak")
    private val mActivity = itemsActivity

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

}