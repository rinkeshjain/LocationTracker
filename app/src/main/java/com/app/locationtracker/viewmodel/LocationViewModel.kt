package com.app.locationtracker.viewmodel

import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.app.locationtracker.TrackerApplication
import com.app.locationtracker.database.AppDatabase
import com.app.locationtracker.database.UserLocation
import com.app.locationtracker.repository.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by Rinkesh on 19/07/20.
 */
class LocationViewModel(application: TrackerApplication) : AndroidViewModel(application) {

    private val repository: LocationRepository

    // Using LiveData and caching what userLocation returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allLocation: LiveData<List<UserLocation>>

    init {
        val locationDao = AppDatabase.getDatabase(application).locationDao()
        repository = LocationRepository(locationDao)
        allLocation = repository.allLocation
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(location: Location?) = viewModelScope.launch(Dispatchers.IO) {
        UserLocation(
            name = "LoginUser",
            trackTime = Calendar.getInstance().timeInMillis,
            latitude = location?.latitude.toString(),
            longitude = location?.longitude.toString()
        ).apply {
            repository.insert(this)
        }

    }


}