package com.app.locationtracker.repository

import androidx.lifecycle.LiveData
import com.app.locationtracker.database.LocationDao
import com.app.locationtracker.database.UserLocation

/**
 * Created by Rinkesh on 19/07/20.
 */
class LocationRepository(private val locationDao: LocationDao) {

    val allLocation: LiveData<List<UserLocation>> = locationDao.getAllLocation()


    suspend fun insert(word: UserLocation) {
        locationDao.insert(word)
    }
}
