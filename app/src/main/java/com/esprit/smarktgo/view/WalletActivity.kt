package com.esprit.smarktgo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.esprit.smarktgo.R
import com.esprit.smarktgo.databinding.ActivityOtpBinding
import com.esprit.smarktgo.databinding.ActivityWalletBinding
import com.esprit.smarktgo.viewmodel.OtpViewModel
import com.esprit.smarktgo.viewmodel.WalletViewModel
import java.net.IDN

class WalletActivity() : AppCompatActivity() {

    private lateinit var binding : ActivityWalletBinding
    lateinit var walletViewModel: WalletViewModel
    lateinit var id : String
    var wallet : Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener { finish() }

        walletViewModel = WalletViewModel(this)
        id = intent.getStringExtra("id")!!
        wallet = intent.getDoubleExtra("wallet",0.0)

        binding.fillButton.setOnClickListener {
            walletViewModel.fill(binding.code.text.toString())
        }

    }

    fun showError(errorText : String)
    {
        binding.codeContainer.error = errorText
    }

}