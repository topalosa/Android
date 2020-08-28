package fi.jamk.googlemapsexample

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private var mLocationPermissionGranted: Boolean = false
    private var PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // location update callback object
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    // Update UI with location data
                    if (location != null) {
                        //latitudeTextView.text = location.latitude.toString()
                        //longitudeTextView.text = location.longitude.toString()
                        //Toast.makeText(applicationContext, location.latitude.toString()+ " "+location.longitude.toString(), Toast.LENGTH_LONG).show()
                        Log.d("MAPS", location.latitude.toString()+ " "+location.longitude.toString())
                    }
                }
            }
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        //val sydney = LatLng(-34.0, 151.0)
        //Map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //Map.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        // Add a marker in Dynamo and move the camera
        val dynamo = LatLng(62.2416223, 25.7597309)
        mMap.addMarker(MarkerOptions().position(dynamo).title("Dynamo at Piippukatu"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dynamo, 14.0F))
        // zoom
        mMap.uiSettings.isZoomControlsEnabled = true
        // click listener
        mMap.setOnMarkerClickListener(this)


        // add a custom marker from XML drawable
        val jyvaskyla = LatLng(62.242561,25.747499)
        val options = MarkerOptions()
        options
            .position(jyvaskyla)
            .title("Jyväskylä")
            .icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.circle)))
        mMap.addMarker(options)

        // add a custom marker from image drawable
        val mapas = LatLng(62.237, 25.763)
        val options2 = MarkerOptions()
        options2
            .position(mapas)
            .title("mapas")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapas))
        mMap.addMarker(options2)

        // check location permission
        getLocationPermission()

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

    }

    private fun getBitmap(drawableRes: Int): Bitmap {
        val drawable = resources.getDrawable(drawableRes,theme)
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }


    override fun onMarkerClick(marker: Marker?): Boolean {
        Toast.makeText(this,marker!!.title, Toast.LENGTH_LONG).show();
        return true
    }

    private fun getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationPermissionGranted = true
            startLocationUpdates()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    mLocationPermissionGranted = true
                    startLocationUpdates()
                }
            }
        }
        updateLocationUI()
    }

    private fun updateLocationUI() {
        if (mMap == null) {
            return
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }
    }

    private fun startLocationUpdates() {
        // Create location request object
        val locationRequest = LocationRequest.create()?.apply {
            interval = 1000
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        mFusedLocationProviderClient.requestLocationUpdates(locationRequest,
            locationCallback,
            null /* Looper */)
        // The Looper object whose message queue will be used to implement the callback mechanism, or null to make callbacks on the calling thread.
    }

    override fun onPause() {
        super.onPause()
        // Stop location updates
        mFusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

}
