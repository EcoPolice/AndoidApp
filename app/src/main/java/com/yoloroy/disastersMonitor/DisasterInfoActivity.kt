package com.yoloroy.disastersMonitor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import com.yoloroy.disastersMonitor.models.Disaster
import com.yoloroy.disastersMonitor.models.DisasterData
import kotlinx.android.synthetic.main.activity_disaster_info.*
import kotlin.time.ExperimentalTime
import kotlin.time.days
import kotlin.time.seconds

class DisasterInfoActivity : AppCompatActivity() {
    lateinit var disaster: Disaster
    var currentImage = 0

    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disaster_info)

        intent.extras!!.run {
            @Suppress("UNCHECKED_CAST")
            disaster = Disaster(
                getInt("id"),
                getString("name")!!,
                getStringArray("images")!!.toList(),
                getSerializable("coordinates")!! as Pair<Double, Double>,
                getInt("date"),
                getString("description")!!,
                DisasterData(
                    getString("objectName")!!,
                    getString("owner")!!,
                    getString("cause")!!,
                    getString("product")!!,
                    getString("volume")!!,
                    getInt("area"),
                    getString("damage")!!,
                    getInt("damagedCount"),
                    getStringArray("damagedObjects")!!.toList()
                )
            )
        }

        disaster.run {
            disasterName.text = name
            disasterDate.text = date.seconds.toIsoString()
            disasterDescription.text = description
            objectName.text = disaster.data.objectName
            owner.text = disaster.data.owner
            cause.text = disaster.data.cause
            product.text = disaster.data.product
            volume.text = disaster.data.volume
            area.text = "${disaster.data.area} m^2"
            damage.text = disaster.data.damage
            damagedCount.text = "damaged: ${disaster.data.damagedCount}"
            damagedObjects.text = disaster.data.damagedObjects.joinToString(",")
        }
    }

    override fun onStart() {
        super.onStart()

        viewImage()

        prevImage.setOnClickListener {
            currentImage--
            if (currentImage < 0)
                currentImage = disaster.images.size
            viewImage()
        }

        nextImage.setOnClickListener {
            currentImage++
            if (currentImage > disaster.images.lastIndex)
                currentImage = 0
            viewImage()
        }

        goToMap.setOnClickListener {
            finishActivity(0)
        }

        addImage.setOnClickListener {
            onNewImage()
        }
    }

    private fun viewImage() {
        if (disaster.images.isNotEmpty())
            Picasso
                .with(this)
                .load(disaster.images[currentImage])
                .into(imageView)
    }

    private fun onNewImage() {

    }
}