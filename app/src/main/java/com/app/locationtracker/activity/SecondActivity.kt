package com.app.locationtracker.activity

import android.location.Location
import android.os.Bundle
import android.util.Log
import com.app.locationtracker.R
import kotlinx.android.synthetic.main.activity_second.*


class SecondActivity : BaseLocationActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        foreground_only_location_button.setOnClickListener {
            requestLocationTrack()
        }
        exit.setOnClickListener {
            finish()
        }
    }


    override fun updatedLocation(location: Location) {
        logResultsToScreen("Latitude: ${location.latitude},\n\nLongitude: ${location.longitude}")
        Log.d("Second Activity", location.toString())
    }

    override fun updatedLocationState(isEnable: Boolean) {
        if (isEnable) {
            foreground_only_location_button.text =
                getString(R.string.stop_location_updates_button_text)
        } else {
            foreground_only_location_button.text =
                getString(R.string.start_location_updates_button_text)
        }

    }

    private fun logResultsToScreen(output: String) {
        output_text_view.text = output
    }


}