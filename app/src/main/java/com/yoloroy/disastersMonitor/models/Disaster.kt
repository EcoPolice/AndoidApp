package com.yoloroy.disastersMonitor.models

data class Disaster(
    val id: Int,
    val name: String,
    val images: List<String>,
    val coordinates: Pair<Double, Double>,
    val date: Int,
    val description: String,
    val data: DisasterData
)

data class DisasterData(
    val objectName: String,
    val owner: String,
    val cause: String,
    val product: String,
    val volume: String,
    val area: Int, // square meters
    val damage: String,
    val damagedCount: Int,
    val damagedObjects: List<String>
)
