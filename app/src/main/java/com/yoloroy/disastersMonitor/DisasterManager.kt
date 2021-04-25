package com.yoloroy.disastersMonitor

import com.yoloroy.disastersMonitor.web.apiClient
import retrofit2.await

object DisasterManager {
    suspend fun getAllDisasters() = apiClient.getAllDisasters().await()
}