package com.app.locationtracker.activity

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.locationtracker.R
import com.app.locationtracker.TrackerApplication
import com.app.locationtracker.adaptor.LocationAdapter
import com.app.locationtracker.viewmodel.LocationViewModel
import kotlinx.android.synthetic.main.activity_first.*

class FirstActivity : BaseLocationActivity() {
    private lateinit var viewModel: LocationViewModel
    val adapter = LocationAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        viewModel = LocationViewModel(application as TrackerApplication)
        initAdaptor()
        observDataChange()
        foreground_only_location_button.setOnClickListener {
            requestLocationTrack()
        }
        nextScreen.setOnClickListener {
            Intent(this, SecondActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    private fun observDataChange() {
        viewModel.allLocation.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.setLocation(it) }
        })
    }

    private fun initAdaptor() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }


    override fun updatedLocation(location: Location) {
        Log.d("First Activity", location.toString())
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
}