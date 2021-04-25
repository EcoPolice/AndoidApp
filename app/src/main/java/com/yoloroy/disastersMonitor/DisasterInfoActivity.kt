package com.yoloroy.disastersMonitor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.yoloroy.disastersMonitor.models.Disaster
import com.yoloroy.disastersMonitor.utils.getDisaster
import kotlinx.android.synthetic.main.activity_disaster_info.*
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

class DisasterInfoActivity : AppCompatActivity() {
    lateinit var disaster: Disaster
    var currentImage = 0

    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disaster_info)

        disaster = intent.extras!!.getDisaster()

        disaster.run {
            disasterName.text = name
            disasterDate.text = date.seconds.toIsoString()
            disasterDescription.text = description
            vobjectName.text = disaster.objectName
            vowner.text = disaster.owner
            vcause.text = disaster.cause
            vproduct.text = disaster.product
            vvolume.text = disaster.volume
            varea.text = "${disaster.area} m^2"
            vdamage.text = "kill me"
            vdamagedCount.text = "damaged: ${disaster.damagedCount}"
            vdamagedObjects.text = disaster.damagedObjects.joinToString(",")
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
            finish()
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