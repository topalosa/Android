package fi.jamk.locationexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    // Fused Location Provider Client instance
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    // Used in Location Updates
    private lateinit var locationCallback: LocationCallback
    // Location Request
    private val MY_PERMISSIONS_REQUEST_LOCATION = 1

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create an instance of the Fused Location Provider Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // check location permissions in run time
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Toast.makeText(this,"App needs a Location to work", Toast.LENGTH_LONG).show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
            }
        } else {
            // Permission has already been granted

        }

        // location update callback object
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    // Update UI with location data
                    if (location != null) {
                        latitudeTextView.text = location.latitude.toString()
                        longitudeTextView.text = location.longitude.toString()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission was granted
                    Toast.makeText(this,"Location permission granted!", Toast.LENGTH_LONG).show()
                } else {
                    // permission denied
                    Toast.makeText(this,"Location permission denied!", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    // Get the last know location
    fun getLastLocation(v: View) {
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    latitudeTextView.text = location.latitude.toString()
                    longitudeTextView.text = location.longitude.toString()
                }
            }
    }

    fun startLocationUpdates(v: View) {
        // Create location request object
        val locationRequest = LocationRequest.create()?.apply {
            interval = 1000
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            null /* Looper */)
        // The Looper object whose message queue will be used to implement the callback mechanism, or null to make callbacks on the calling thread.
    }

    override fun onPause() {
        super.onPause()
        // Stop location updates
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}
