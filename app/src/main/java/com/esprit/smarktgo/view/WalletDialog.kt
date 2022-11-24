package com.esprit.smarktgo.view

import android.app.AlertDialog
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.esprit.smarktgo.R
import com.esprit.smarktgo.model.Item
import com.esprit.smarktgo.model.Order
import com.esprit.smarktgo.viewmodel.WalletViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class WalletDialog(val mFragment: ProfileFragment,val userId: String, val wallet: Double) {

    private lateinit var dialog: AlertDialog
    lateinit var codeContainer : TextInputLayout
    lateinit var code : TextInputEditText
    lateinit var fillButton : Button
    private val walletViewModel = WalletViewModel(this)

    fun show(){
        /**set View*/
        val infalter = mFragment.layoutInflater
        val dialogView = infalter.inflate(R.layout.wallet_dialog,null)
        /**set Dialog*/
        val bulider = AlertDialog.Builder(mFragment.requireContext())
        bulider.setView(dialogView)
        bulider.setCancelable(true)
        dialog = bulider.create()
        dialog.show()

        codeContainer = dialog.findViewById(R.id.codeContainer)
        code = dialog.findViewById(R.id.code)
        fillButton = dialog.findViewById(R.id.fillButton)


        fillButton.setOnClickListener {
            walletViewModel.fill(code.text.toString())
        }

    }

    fun dismiss(wallet:Double){
        dialog.dismiss()
        mFragment.walletUpdated(wallet)
    }

    fun showError(errorText : String)
    {
        codeContainer.error = errorText
    }


}