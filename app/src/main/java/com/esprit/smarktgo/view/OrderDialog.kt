package com.esprit.smarktgo.view

import android.app.AlertDialog
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.esprit.smarktgo.R

class OrderDialog(val mActivity: ItemsActivity) {

    private lateinit var dialog: AlertDialog
    lateinit var priceTV : TextView
    lateinit var quantityTV : TextView
    lateinit var addToCartButton : Button
    var price : Double = 0.0
    var counterPrice : Double = 0.0
    var quantity = 1

    lateinit var plusButton: CardView
    lateinit var minusButton: CardView
    fun show(){
        /**set View*/
        val infalter = mActivity.layoutInflater
        val dialogView = infalter.inflate(R.layout.order_dialog,null)
        /**set Dialog*/
        val bulider = AlertDialog.Builder(mActivity)
        bulider.setView(dialogView)
        bulider.setCancelable(false)
        dialog = bulider.create()
        dialog.show()

        priceTV = dialog.findViewById(R.id.price)
        quantityTV = dialog.findViewById(R.id.quantity)
        plusButton = dialog.findViewById(R.id.plusButton)
        minusButton = dialog.findViewById(R.id.minusButton)
        addToCartButton = dialog.findViewById(R.id.addToCartButton)

        priceTV.text = price.toString()
        quantityTV.text = quantity.toString()

        plusButton.setOnClickListener {
            plus()
        }
        minusButton.setOnClickListener {
            minus()
        }

    }
    fun dismiss(){
        dialog.dismiss()
    }

    fun plus()
    {
        quantity++
        counterPrice = price* quantity

        priceTV.text = "%.1f".format(counterPrice)
        quantityTV.text = quantity.toString()
    }

    fun minus()
    {
        if(quantity>1)
        {
            quantity--
            counterPrice = price* quantity
            priceTV.text = "%.1f".format(counterPrice)
            quantityTV.text = quantity.toString()
        }
    }


}