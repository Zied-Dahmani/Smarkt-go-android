package com.esprit.smarktgo.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Order (
    @SerializedName("userId") val userId: String,
    @SerializedName("items") val items: ArrayList<Item>,
): Serializable