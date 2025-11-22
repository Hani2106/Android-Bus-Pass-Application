package com.example.buspassapplication

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * This callback is triggered when the map is ready to be used.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // --- NEW: Enable user controls for zooming ---
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true

        // --- NEW: Define multiple routes ---

        // Route 1 (Blue)
        val startPoint1 = LatLng(21.1702, 72.8311) // Surat Station
        val endPoint1 = LatLng(21.2418, 72.8885)   // Sarthana Jakatnaka
        mMap.addMarker(MarkerOptions().position(startPoint1).title("Route 1 Start"))
        mMap.addMarker(MarkerOptions().position(endPoint1).title("Route 1 End"))
        val routePolyline1 = PolylineOptions()
            .add(startPoint1)
            .add(LatLng(21.2050, 72.8590)) // Intermediate stop
            .add(endPoint1)
            .width(10f)
            .color(Color.BLUE)
        mMap.addPolyline(routePolyline1)
        // Route 2 (Red)
        val startPoint2 = LatLng(21.1959, 72.7821) // Adajan
        val endPoint2 = LatLng(21.1593, 72.8159)   // Vesu
        mMap.addMarker(MarkerOptions().position(startPoint2).title("Route 2 Start"))
        mMap.addMarker(MarkerOptions().position(endPoint2).title("Route 2 End"))
        val routePolyline2 = PolylineOptions()
            .add(startPoint2)
            .add(LatLng(21.1717, 72.7877)) // Intermediate stop
            .add(endPoint2)
            .width(10f)
            .color(Color.RED)
        mMap.addPolyline(routePolyline2)

        // Route 3 (Green)
        val startPoint3 = LatLng(21.2267, 72.8340) // Katargam
        val endPoint3 = LatLng(21.1449, 72.7820)   // Dumas Beach
        mMap.addMarker(MarkerOptions().position(startPoint3).title("Route 3 Start"))
        mMap.addMarker(MarkerOptions().position(endPoint3).title("Route 3 End"))
        val routePolyline3 = PolylineOptions()
            .add(startPoint3)
            .add(LatLng(21.1895, 72.8088)) // Intermediate stop
            .add(endPoint3)
            .width(10f)
            .color(Color.GREEN)
        mMap.addPolyline(routePolyline3)


        // --- NEW: Move the camera to show ALL routes at once ---
        val boundsBuilder = LatLngBounds.Builder()
        boundsBuilder.include(startPoint1)
        boundsBuilder.include(endPoint1)
        boundsBuilder.include(startPoint2)
        boundsBuilder.include(endPoint2)
        boundsBuilder.include(startPoint3)
        boundsBuilder.include(endPoint3)
        val bounds = boundsBuilder.build()
        val padding = 150 // Padding in pixels from the edge of the screen
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)

        // Use post to ensure the map has laid out before moving the camera
        mMap.setOnMapLoadedCallback {
            mMap.animateCamera(cameraUpdate)
        }
    }
}

