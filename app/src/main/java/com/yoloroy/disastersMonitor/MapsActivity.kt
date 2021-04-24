package com.yoloroy.disastersMonitor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.yoloroy.disastersMonitor.models.Disaster

private val Pair<Double, Double>.latLng get() = LatLng(first, second)

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var disastersViewModel: DisastersViewModel

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        disastersViewModel = ViewModelProvider(this).get(DisastersViewModel::class.java)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        disastersViewModel.disasters.observeForever {
            loadDisasters(it!!)
        }
    }

    private fun loadDisasters(disasters: List<Disaster>) {
        mMap.clear()

        disasters.forEach { disaster ->
            mMap.addMarker(
                MarkerOptions()
                    .position(disaster.coordinates.latLng)
                    .title(disaster.id.toString())
            )
            mMap.setOnMarkerClickListener { marker ->
                val currentEvent = disasters.find { it.id.toString() == marker.title }!!

                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentEvent.coordinates.latLng))

                val intent = Intent(this, DisasterInfoActivity::class.java)
                intent.putExtra("id", currentEvent.id)
                intent.putExtra("name", currentEvent.name)
                intent.putExtra("images", currentEvent.images.toTypedArray())
                intent.putExtra("coordinates", currentEvent.coordinates)
                intent.putExtra("date", currentEvent.date)
                intent.putExtra("description", currentEvent.description)
                intent.putExtra("objectName", currentEvent.data.objectName)
                intent.putExtra("owner", currentEvent.data.owner)
                intent.putExtra("cause", currentEvent.data.cause)
                intent.putExtra("product", currentEvent.data.product)
                intent.putExtra("volume", currentEvent.data.volume)
                intent.putExtra("area", currentEvent.data.area)
                intent.putExtra("damage", currentEvent.data.damage)
                intent.putExtra("damagedCount", currentEvent.data.damagedCount)
                intent.putExtra("damagedObjects", currentEvent.data.damagedObjects.toTypedArray())

                startActivityForResult(intent, 0)

                true
            }
        }
    }
}