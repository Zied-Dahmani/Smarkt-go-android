package com.esprit.smarktgo.viewmodel

import android.content.ContentValues
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.model.Supermarket
import com.esprit.smarktgo.repository.SupermarketRepository
import com.esprit.smarktgo.view.HomeFragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

class HomeViewModel(homeFragment: HomeFragment): ViewModel() {

    private var supermarketsLiveData = MutableLiveData<List<Supermarket>>()
    val mFragment = homeFragment
    var fusedLocationProviderClient: FusedLocationProviderClient

    init {
        getAll()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(homeFragment.requireActivity())
        checkLocationPermission()
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

    private fun checkLocationPermission()
    {
        val task = fusedLocationProviderClient.lastLocation

        if(ActivityCompat.checkSelfPermission(mFragment.requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mFragment.requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(mFragment.requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),101)
        }
        task.addOnSuccessListener {
            if(it!=null)
            {
               // mFragment.view?.findViewById<TextView>(R.id.quoteTV)?.setText(it.latitude.toString())
            }
        }
        return
    }

}