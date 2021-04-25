package com.yoloroy.disastersMonitor.utils

import android.content.Intent
import android.os.Bundle
import com.yoloroy.disastersMonitor.models.Disaster

fun Intent.putDisaster(disaster: Disaster) {
    putExtra("id", disaster.id)
    putExtra("name", disaster.name)
    putExtra("images", disaster.images?.toTypedArray() ?: emptyArray())
    putExtra("coordinates", disaster.coordinates)
    putExtra("date", disaster.date)
    putExtra("description", disaster.description)
    putExtra("objectName", disaster.objectName)
    putExtra("owner", disaster.owner)
    putExtra("cause", disaster.cause)
    putExtra("product", disaster.product)
    putExtra("volume", disaster.volume)
    putExtra("area", disaster.area)
    putExtra("damagedCount", disaster.damagedCount)
    putExtra("damagedObjects", disaster.damagedObjects.toTypedArray())
}

@Suppress("UNCHECKED_CAST")
fun Bundle.getDisaster() = Disaster(
    getInt("id"),
    getString("name") ?: getString("objectName")!!,
    getStringArray("images")!!.toList(),
    getSerializable("coordinates")!! as Pair<Double, Double>,
    getInt("date"),
    getString("description").toString(),
    getString("objectName") ?: getString("name")!!,
    getString("owner")!!,
    getString("cause")!!,
    getString("product")!!,
    getString("volume").toString(),
    getInt("area"),
    getInt("damagedCount"),
    getStringArray("damagedObjects")!!.toList()
)
