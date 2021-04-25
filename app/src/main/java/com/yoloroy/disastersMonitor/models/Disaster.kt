package com.yoloroy.disastersMonitor.models

import com.google.gson.annotations.SerializedName

data class Disaster(
    @SerializedName("id") val id: Int,
    @SerializedName("disasterName") val name: String,
    @SerializedName("images") var images: List<String>?,
    @SerializedName("lat") val lat: Double,
    @SerializedName("long") val lng: Double,
    @SerializedName("disasterDate") val date: Long,
    @SerializedName("disasterDescription") val description: String,
    @SerializedName("objectName") val objectName: String,
    @SerializedName("owner") val owner: String,
    @SerializedName("cause") val cause: String,
    @SerializedName("product") val product: String,
    @SerializedName("volume") val volume: String,
    @SerializedName("area") val area: Int, // square meters
    @SerializedName("damageCount") val damagedCount: Int,
    @SerializedName("damagedObjects") val damagedObjects: List<String>?
) {
    constructor(
        id: Int,
        name: String,
        images: List<String>,
        coords: Pair<Double, Double>,
        date: Long,
        description: String,
        objectName: String,
        owner: String,
        cause: String,
        product: String,
        volume: String,
        area: Int,
        damagedCount: Int,
        damagedObjects: List<String>
    ) : this(
        id,
        name,
        images,
        coords.first,
        coords.second,
        date,
        description,
        objectName,
        owner,
        cause,
        product,
        volume,
        area,
        damagedCount,
        damagedObjects
    )

    val coordinates: Pair<Double, Double> get() = lat to lng
}
