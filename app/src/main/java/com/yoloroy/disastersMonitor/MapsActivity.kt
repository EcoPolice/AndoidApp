package com.yoloroy.disastersMonitor

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.yoloroy.disastersMonitor.models.Disaster
import com.yoloroy.disastersMonitor.utils.putDisaster
import com.yoloroy.disastersMonitor.web.apiClient
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.await
import java.nio.ByteBuffer


private val Pair<Double, Double>.latLng get() = LatLng(first, second)

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        const val CODE_GET_PICTURE = 1
    }

    private lateinit var disastersViewModel: DisastersViewModel

    private lateinit var mMap: GoogleMap

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null)
            when (requestCode) {
                CODE_GET_PICTURE -> putImage(data.data!!)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        disastersViewModel = ViewModelProvider(this).get(DisastersViewModel::class.java).apply {
            disasters.update(lifecycleScope)
        }
    }

    override fun onStart() {
        super.onStart()

        addDisasterFAB.setOnClickListener {
            chooseImage()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        disastersViewModel.disasters.observeForever {
            if (!it.isNullOrEmpty())
                loadDisasters(it)
        }
    }

    private fun loadDisasters(disasters: List<Disaster>) {
        mMap.clear()

        disasters.forEach { disaster ->
            try {
                mMap.addMarker(
                    MarkerOptions()
                        .position(disaster.coordinates.latLng)
                        .title(disaster.id.toString())
                )
                mMap.setOnMarkerClickListener { marker ->
                    val currentEvent = disasters.find { it.id.toString() == marker.title }!!

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentEvent.coordinates.latLng))

                    val intent = Intent(this, DisasterInfoActivity::class.java)
                    intent.putDisaster(currentEvent)

                    startActivity(intent)

                    true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun chooseImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }

        startActivityForResult(
            Intent.createChooser(intent, "Select picture"),
            1
        )
    }

    private fun putImage(data: Uri) {
        Log.i("file", data.toString())
        var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data)

        val width = bitmap.getWidth()
        val height = bitmap.getHeight()

        val size: Int = bitmap.getRowBytes() * bitmap.getHeight()
        val byteBuffer: ByteBuffer = ByteBuffer.allocate(size)
        bitmap.copyPixelsToBuffer(byteBuffer)
        val byteArray = byteBuffer.array()

        //Log.i("file", file.toString())

        val requestFile: RequestBody = RequestBody.create(
            MediaType.parse("*/*"),
            byteArray
        )

        val body = MultipartBody.Part.createFormData(
            byteArray.hashCode().toString(),
            data.lastPathSegment!!,
            requestFile
        )

        val descriptionString = "pic"
        val description = RequestBody.create(MultipartBody.FORM, descriptionString)

        lifecycleScope.launch {
            apiClient.uploadImage(body).await()
        }
    }
}