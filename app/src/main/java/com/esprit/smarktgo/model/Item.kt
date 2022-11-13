package com.esprit.smarktgo.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Item(
    @SerializedName("name") val name: String,
    @SerializedName("image") val image:String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double,
    @SerializedName("category") val category: String,
    @SerializedName("supermarketId") val supermarketId: String
): Serializable




data class ItemInfo(
    @SerializedName("category") val category: String,
    @SerializedName("supermarketId") val supermarketId:String,
):Serializable