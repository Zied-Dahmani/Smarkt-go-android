package com.esprit.smarktgo.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.model.Supermarket
import com.esprit.smarktgo.repository.SupermarketRepository
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {

    private var supermarketsLiveData = MutableLiveData<List<Supermarket>>()

    init {
        getAll()
    }

    fun getAll() {
        try {
            val supermarketRepository = SupermarketRepository()

            viewModelScope.launch {
                val result = supermarketRepository.getAll()
                if(result!=null)
                {
                    supermarketsLiveData.value = result
                }
            }
        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }

    fun observeSupermarketsLiveData() : LiveData<List<Supermarket>> {
        return supermarketsLiveData
    }

}