package com.esprit.smarktgo.viewmodel

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.R
import com.esprit.smarktgo.model.UpdateTicket
import com.esprit.smarktgo.repository.TicketRepository
import com.esprit.smarktgo.view.WalletActivity
import kotlinx.coroutines.launch

class WalletViewModel(mActivity: WalletActivity): ViewModel() {

    private val mActivity = mActivity
    var CAMERA_REQUEST_CODE = 100
    var STORAGE_REQUEST_CODE = 101
     var imageUri: Uri?=null
    private lateinit var cameraPermissions:Array<String>
    private lateinit var storagePermissions:Array<String>
    private val ticketRepository: TicketRepository = TicketRepository()


    fun fill(code: String) : Boolean {
        if(code.isEmpty())
        {
            mActivity.showError(mActivity.getString(R.string.type_the_ticket_code))
            return false
        }
        else if(code.length!=6)
        {
            mActivity.showError(mActivity.getString(R.string.type_a_valid_code))
            return false
        }
        else{
            viewModelScope.launch {
                var found = false
                val result = ticketRepository.getAll()
                result.let {
                    for(ticket in result!!)
                    {
                        if(code==ticket.code.toString()&&!ticket.used)
                        {
                            found = true
                            update(code)
                            break
                        }
                        else if(code==ticket.code.toString())
                        {
                            mActivity.showError(mActivity.getString(R.string.invalid_ticket))
                            found = true
                            break
                        }
                    }
                    if(!found)
                        mActivity.showError(mActivity.getString(R.string.ticket_not_found))
                }
            }
        }
        return true
    }

    private fun update(code: String)
    {
        viewModelScope.launch {
            val updateTicket = UpdateTicket(code.toInt(),mActivity.id,mActivity.wallet)
            val result = ticketRepository.update(updateTicket)
            result?.let {
                mActivity.finish()
            }
        }
    }
     fun checkStoragePermission():Boolean {
        return  ContextCompat.checkSelfPermission(mActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
    }
     fun checkCameraPermission():Boolean {
        val storagePermission =   ContextCompat.checkSelfPermission(mActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
        val cameraPermission= ContextCompat.checkSelfPermission(mActivity,
            Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED

        return storagePermission && cameraPermission
    }
     fun requestStoragePermission(){
        ActivityCompat.requestPermissions(mActivity,storagePermissions,STORAGE_REQUEST_CODE)
    }
     fun requestCameraPermission(){
        ActivityCompat.requestPermissions(mActivity,cameraPermissions,CAMERA_REQUEST_CODE)
    }
     fun pickImageGallery(){
        val intent= Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        galleryActivityResultLauncher.launch(intent)
    }

     fun pickImageCamera(){
        val values= ContentValues()
        values.put(MediaStore.Images.Media.TITLE,"SIMPLE")
        values.put(MediaStore.Images.Media.DESCRIPTION,"DESCRIPTION")

        imageUri=mActivity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)

        val intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
        cameraActivityResultLauncher.launch(intent)
    }
    private val cameraActivityResultLauncher =
        mActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {

            }
            else {

            }
        }

    private val galleryActivityResultLauncher =
        mActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result->

            if (result.resultCode== Activity.RESULT_OK)
            {
                val data=result.data
                imageUri=data!!.data
            }

            else {
                showToast("Cancelled...")
            }

        }



    private fun showToast(message:String){
       Toast.makeText(mActivity,message, Toast.LENGTH_SHORT).show()
    }



}