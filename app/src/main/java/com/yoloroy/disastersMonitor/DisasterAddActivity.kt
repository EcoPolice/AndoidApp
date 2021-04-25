package com.yoloroy.disastersMonitor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import com.yoloroy.disastersMonitor.models.Disaster
import com.yoloroy.disastersMonitor.web.apiClient
import kotlinx.android.synthetic.main.activity_disaster_edit.*
import kotlinx.coroutines.launch
import retrofit2.await
import java.util.*
import kotlin.time.ExperimentalTime

class DisasterAddActivity : AppCompatActivity() {
    lateinit var latlng: Pair<Double, Double>
    val images = mutableListOf<String>()
    var currentImage = 0

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null)
            when (requestCode) {
                MapsActivity.CODE_GET_PICTURE -> lifecycleScope.launch {
                    val newImg = putImage(data.data!!, contentResolver)
                    if (!newImg.isNullOrBlank()) {
                        images += listOf(newImg.replace("[", "").replace("]", ""))
                        viewImage()
                    } else
                        Toast.makeText(this@DisasterAddActivity, "Img not sent", Toast.LENGTH_SHORT)
                            .show()
                    Log.i("img", images.toString())
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disaster_edit)

        latlng = intent.getSerializableExtra("coordinates") as Pair<Double, Double>
    }

    override fun onStart() {
        super.onStart()

        viewImage()

        prevImage.setOnClickListener {
            currentImage--
            if (currentImage < 0)
                currentImage = images.size ?: 0
            viewImage()
        }

        nextImage.setOnClickListener {
            currentImage++
            if (currentImage > images.lastIndex ?: -1)
                currentImage = 0
            viewImage()
        }

        goToMap.setOnClickListener {
            lifecycleScope.launch {
                apiClient.addDisaster(
                    Disaster(
                        -1,
                        disasterName.text.toString(),
                        images,
                        latlng,
                        Date().time,
                        disasterDescription.text.toString(),
                        vobjectName.text.toString(),
                        vowner.text.toString(),
                        vcause.text.toString(),
                        vproduct.text.toString(),
                        vvolume.text.toString(),
                        varea.text.toString().toIntOrNull() ?: 0,
                        vdamagedCount.text.toString().toIntOrNull() ?: 0,
                        vdamagedObjects.text.toString().split(",")
                    ).also { Log.i("dis", it.toString()) }
                ).await()
                finish()
            }
        }

        addImage.setOnClickListener {
            onNewImage()
        }
    }

    private fun viewImage() {
        if (!images.isNullOrEmpty())
            Picasso
                .with(this)
                .load(images[currentImage])
                .into(imageView)
    }

    private fun onNewImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }

        startActivityForResult(
            Intent.createChooser(intent, "Select picture"),
            MapsActivity.CODE_GET_PICTURE
        )
    }
}