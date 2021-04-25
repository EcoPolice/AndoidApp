package com.yoloroy.disastersMonitor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import com.yoloroy.disastersMonitor.MapsActivity.Companion.CODE_GET_PICTURE
import com.yoloroy.disastersMonitor.models.Disaster
import com.yoloroy.disastersMonitor.utils.getDisaster
import kotlinx.android.synthetic.main.activity_disaster_info.*
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

class DisasterInfoActivity : AppCompatActivity() {
    lateinit var disaster: Disaster
    var currentImage = 0

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null)
            when (requestCode) {
                CODE_GET_PICTURE -> lifecycleScope.launch {
                    val newImg = putImage(data.data!!, contentResolver)
                    if (!newImg.isNullOrBlank()) {
                        disaster.images = (disaster.images ?: emptyList()) + listOf(
                            newImg.replace("[", "").replace("]", "")
                        )
                        viewImage()
                    } else
                        Toast.makeText(
                            this@DisasterInfoActivity,
                            "Img not sent",
                            Toast.LENGTH_SHORT
                        ).show()
                    Log.i("img", disaster.images.toString())
                }
            }
    }

    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disaster_info)

        disaster = intent.extras!!.getDisaster()

        disaster.run {
            disasterName.text = name
            disasterDate.text = date.seconds.toIsoString()
            disasterDescription.text = description
            vobjectName.text = objectName
            vowner.text = owner
            vcause.text = cause
            vproduct.text = product
            vvolume.text = volume
            varea.text = "$area m^2"
            vdamage.text = "kill me"
            vdamagedCount.text = "damaged: $damagedCount"
            vdamagedObjects.text = damagedObjects?.joinToString(",") ?: ""
        }
    }

    override fun onStart() {
        super.onStart()

        viewImage()

        prevImage.setOnClickListener {
            currentImage--
            if (currentImage < 0)
                currentImage = disaster.images?.size ?: 0
            viewImage()
        }

        nextImage.setOnClickListener {
            currentImage++
            if (currentImage > disaster.images?.lastIndex ?: -1)
                currentImage = 0
            viewImage()
        }

        goToMap.setOnClickListener {
            finish()
        }
    }

    private fun viewImage() {
        if (!disaster.images.isNullOrEmpty())
            Picasso
                .with(this)
                .load(disaster.images!![currentImage])
                .into(imageView)
    }

    private fun onNewImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }

        startActivityForResult(
            Intent.createChooser(intent, "Select picture"),
            CODE_GET_PICTURE
        )
    }
}