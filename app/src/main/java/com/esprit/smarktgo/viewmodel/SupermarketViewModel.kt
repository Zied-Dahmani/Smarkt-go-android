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
import com.esprit.smarktgo.model.AddRemoveFavorite
import com.esprit.smarktgo.model.IsFavoriteBody
import com.esprit.smarktgo.repository.SupermarketRepository
import com.esprit.smarktgo.view.FavoritesFragment
import com.esprit.smarktgo.view.SupermarketActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class SupermarketViewModel(supermarketActivity: SupermarketActivity): ViewModel()  {

    var categoriesLiveData = MutableLiveData<List<String>>()
    var favorites = MutableLiveData<ArrayList<String>>()
    lateinit var userId: String
    val mActivity = supermarketActivity
    var isFavorite = MutableLiveData<Boolean>()
    val supermarketRepository = SupermarketRepository()

    init {
        getCategories()
        isFavorite()
    }

    private fun getCategories() {
        try {
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

    private fun isFavorite() {
        try {
            GoogleSignIn.getLastSignedInAccount(mActivity).let {
                userId= it?.email!!
            }?: run { userId = FirebaseAuth.getInstance().currentUser?.phoneNumber!! }

            viewModelScope.launch {
                val result = supermarketRepository.isFavorite(IsFavoriteBody(mActivity.supermarketId,userId))
                result.let {
                    favorites.value = result as ArrayList<String>?
                    isFavorite.value = result?.contains(userId)
                }

            }

        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }

    fun addFavorite() {
        favorites.value?.add(userId)
        isFavorite.value = !isFavorite.value!!
        addRemoveFavorite()
    }

    fun removeFavorite() {
        favorites.value?.remove(userId)
        isFavorite.value = !isFavorite.value!!
        addRemoveFavorite()
    }

    fun addRemoveFavorite(){
        try {

            viewModelScope.launch {
                supermarketRepository.addRemoveFavorite(AddRemoveFavorite(mActivity.supermarketId, favorites.value!!))
            }

        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }

    fun observeCategoriesLiveData() : LiveData<List<String>> = categoriesLiveData

    fun observeIsFavoriteLiveData() : LiveData<Boolean> = isFavorite

}