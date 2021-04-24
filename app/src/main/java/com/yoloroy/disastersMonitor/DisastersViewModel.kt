package com.yoloroy.disastersMonitor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yoloroy.disastersMonitor.models.Disaster

class DisastersViewModel : ViewModel() {
    val disasters: MutableLiveData<List<Disaster>> = MutableLiveData()

    init {
        disasters.update()
    }
}

fun MutableLiveData<List<Disaster>>.update() {
    value = DisasterManager.getAllDisasters()
}
