package com.yoloroy.disastersMonitor.models

import com.google.gson.annotations.SerializedName

data class Disaster(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("images") val images: List<String>,
    @SerializedName("coordinates") val coordinates: Pair<Double, Double>,
    @SerializedName("date") val date: Int,
    @SerializedName("description") val description: String,
    @SerializedName("objectName") val objectName: String,
    @SerializedName("owner") val owner: String,
    @SerializedName("cause") val cause: String,
    @SerializedName("product") val product: String,
    @SerializedName("volume") val volume: String,
    @SerializedName("area") val area: Int, // square meters
    @SerializedName("damagedCount") val damagedCount: Int,
    @SerializedName("damagedObjects") val damagedObjects: List<String>
)
