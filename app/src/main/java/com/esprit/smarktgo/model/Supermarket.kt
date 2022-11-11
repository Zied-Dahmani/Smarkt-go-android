package com.esprit.smarktgo.model
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Supermarket (
    @SerializedName("name") val name: String,
    @SerializedName("image") val image:String,
    @SerializedName("description") val description: String,
    @SerializedName("address") val address: String,
    @SerializedName("location") val location: Location
    ): Serializable


data class Location(
    @SerializedName("type") val type: String,
    @SerializedName("coordinates") val coordinates: List<Int>
    ):Serializable
