package com.esprit.smarktgo.viewmodel

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.repository.SupermarketRepository
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

class SupermarketViewModel(): ViewModel()  {

    var categoriesLiveData = MutableLiveData<List<String>>()

    init {
        getCategories()
    }

    private fun getCategories() {
        try {
            val supermarketRepository = SupermarketRepository()

            viewModelScope.launch {
                val result = supermarketRepository.getCategories()
                result.let {
                    categoriesLiveData.value = result
                }
            }

        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }

    fun observeCategoriesLiveData() : LiveData<List<String>> = categoriesLiveData

}