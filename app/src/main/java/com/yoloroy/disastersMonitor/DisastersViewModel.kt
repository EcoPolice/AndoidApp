package com.yoloroy.disastersMonitor

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yoloroy.disastersMonitor.models.Disaster
import kotlinx.coroutines.launch

class DisastersViewModel : ViewModel() {
    val disasters: MutableLiveData<List<Disaster>> = MutableLiveData()
}

fun MutableLiveData<List<Disaster>>.update(lifecycleScope: LifecycleCoroutineScope) {
    lifecycleScope.launch {
        value = DisasterManager.getAllDisasters()
    }
}
