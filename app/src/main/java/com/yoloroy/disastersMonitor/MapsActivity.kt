package com.yoloroy.disastersMonitor

import android.content.Intent
import android.os.Bundle
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


private val Pair<Double, Double>.latLng get() = LatLng(first, second)

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        const val CODE_GET_PICTURE = 1
        const val CREATE_NEW_DISASTER = 2
    }

    private lateinit var disastersViewModel: DisastersViewModel

    private lateinit var mMap: GoogleMap

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null)
            when (requestCode) {
                CREATE_NEW_DISASTER -> disastersViewModel.disasters.update(lifecycleScope)
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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMapClickListener {
            onNewDisaster(it.latitude, it.longitude)
        }

        disastersViewModel.disasters.observeForever {
            if (!it.isNullOrEmpty())
                loadDisasters(it)
        }
    }

    private fun onNewDisaster(lat: Double, lng: Double) {
        val intent = Intent(this, DisasterAddActivity::class.java)
        intent.putExtra("coordinates", lat to lng)

        startActivityForResult(
            intent,
            CREATE_NEW_DISASTER
        )
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
}