package com.esprit.smarktgo.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("id") val id: String,
    @SerializedName ("fullName") val fullName:String,
    @SerializedName ("wallet") val wallet:Float
    ): Serializable

data class ProfileItem (
    val item:String,
    val image:String
        )