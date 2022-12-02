package com.esprit.smarktgo.view


import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.esprit.smarktgo.R
import com.esprit.smarktgo.databinding.ActivityWalletBinding
import com.esprit.smarktgo.viewmodel.WalletViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


class WalletActivity : AppCompatActivity() {
    var CAMERA_REQUEST_CODE = 100
    var STORAGE_REQUEST_CODE = 101
    private lateinit var cameraPermissions: Array<String>
    private lateinit var storagePermissions: Array<String>
    private lateinit var progressDialog: ProgressDialog
    private lateinit var textRecognizer: TextRecognizer

    private lateinit var binding: ActivityWalletBinding
    lateinit var walletViewModel: WalletViewModel
    lateinit var id: String
    var wallet: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener { finish() }

        walletViewModel = WalletViewModel(this)
        id = intent.getStringExtra("id")!!
        wallet = intent.getDoubleExtra("wallet", 0.0)

        binding.fillButton.setOnClickListener {
            walletViewModel.fill(binding.code.text.toString())
        }
        cameraPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        binding.Pick.setOnClickListener {
            showInputImageDialog()

        }
        binding.Scan.setOnClickListener {
           if ( walletViewModel.imageUri == null) {
                showToast("PICK IMAGE FIRST")
            } else {
                recognizeTextFromImage()
            }
        }
    }

    private fun recognizeTextFromImage() {
        progressDialog.setMessage("Preparing Image...")
        progressDialog.show()
        try {
            val inputImage = InputImage.fromFilePath(this,walletViewModel.imageUri!!)
            progressDialog.setMessage("Recognizing text")
            val textTaskResult = textRecognizer.process(inputImage)
                .addOnSuccessListener { text ->
                    progressDialog.dismiss()
                    val recognizedText = text.text
                    binding.code.setText(recognizedText)
                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    showToast("Failed to recognize text due to ${e.message}")
                }
        } catch (e: Exception) {
            progressDialog.dismiss()
            showToast("Failed to prepare image due to ${e.message}")
        }
    }

    private fun showInputImageDialog() {

        val popupMenu = PopupMenu(this, binding.Scan)
        popupMenu.menu.add(Menu.NONE, 1, 1, "CAMERA")
        popupMenu.menu.add(Menu.NONE, 2, 2, "GALLERY")
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener { menuItem ->
            val id = menuItem.itemId
            if (id == 1) {
                if (walletViewModel.checkCameraPermission()) {
                    walletViewModel.pickImageCamera()
                } else {
                    walletViewModel.requestCameraPermission()
                }
            } else if (id == 2) {
                if (walletViewModel.checkStoragePermission()) {
                    walletViewModel.pickImageGallery()
                } else {
                    walletViewModel.requestStoragePermission()
                }
            }
            return@setOnMenuItemClickListener true
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                    if (cameraAccepted && storageAccepted) {
                        walletViewModel.pickImageCamera()
                    } else {
                        showToast("CAMERA PERMISSION REQUIRED")
                    }
                }
            }
            STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (storageAccepted) {
                        walletViewModel.pickImageGallery()

                    } else {
                        showToast("STORAGE PERMISSION IS REQUIRED")
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showError(errorText: String) {
        binding.codeContainer.error = errorText
    }

}