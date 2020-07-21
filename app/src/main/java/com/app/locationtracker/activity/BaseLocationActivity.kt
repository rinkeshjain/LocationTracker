package com.app.locationtracker.activity

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.locationtracker.service.LocationService
import com.app.locationtracker.R
import com.app.locationtracker.util.SharedPreferenceUtil
import com.app.locationtracker.TrackerApplication
import com.google.android.material.snackbar.Snackbar


private const val TAG = "MainActivity"
private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

/**
 * Created by Rinkesh on 18/07/20.
 */
abstract class BaseLocationActivity : AppCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    abstract fun updatedLocation(location: Location)
    abstract fun updatedLocationState(isEnable: Boolean)

    private lateinit var sharedPreferences: SharedPreferences

    // Listens for location broadcasts from LocationService.
    private var locationBroadcastReceiver = lazy { LocationBroadcastReceiver() }

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        updatedLocationState(getLocationState())

        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            locationBroadcastReceiver.value,
            IntentFilter(LocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        )
    }

    protected fun requestLocationTrack() {
        val enabled = sharedPreferences.getBoolean(
            SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false
        )

        if (foregroundPermissionApproved()) {
            (application as TrackerApplication).locationTrackStatusUpdate(enabled)
        } else {
            requestForegroundPermissions()
        }
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationBroadcastReceiver.value)
        super.onPause()
    }

    override fun onStop() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onStop()
    }

    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }


    private fun requestForegroundPermissions() {
        val provideRationale = foregroundPermissionApproved()

        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
            Snackbar.make(
                findViewById(android.R.id.content),
                R.string.permission_rationale,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.ok) {
                    // Request permission
                    ActivityCompat.requestPermissions(
                        this@BaseLocationActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
                    )
                }
                .show()
        } else {
            Log.d(TAG, "Request foreground only permission")
            ActivityCompat.requestPermissions(
                this@BaseLocationActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        }
    }


    /**
     * Receiver for location broadcasts from [LocationService].
     */
    private inner class LocationBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            intent.getParcelableExtra<Location>(LocationService.EXTRA_LOCATION)?.let {
                updatedLocation(it)
            }
        }
    }

    private fun getLocationState() = sharedPreferences.getBoolean(
        SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false
    )

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == SharedPreferenceUtil.KEY_FOREGROUND_ENABLED) {
            updatedLocationState(getLocationState())
        }

    }

}
