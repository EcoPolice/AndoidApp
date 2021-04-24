package com.yoloroy.disastersMonitor

import com.yoloroy.disastersMonitor.models.Disaster
import com.yoloroy.disastersMonitor.models.DisasterData

object DisasterManager {
    fun getAllDisasters() = listOf(// TODO: add getDisastersInArea/getNearbyDisasters
        Disaster(0, "dis1", listOf(), 25.0 to 25.0, 1,"something1",
            DisasterData("disobj1", "owner1", "cause1", "product1",
                "volume1", 11, "damage1", 11, listOf()
            )
        ),
        Disaster(1, "dis2", listOf("https://1tulatv.ru/sites/default/files/kfuljjclzcs.jpg", "https://i10.fotocdn.net/s115/9e7435e0d7cf6747/public_pin_l/2611079296.jpg"), 50.0 to 50.0, 2,"something2",
            DisasterData("disobj2", "owner2", "cause2", "product2",
                "volume2", 22, "damage2", 22, listOf()
            )
        )
    )
}