package com.app.locationtracker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by Rinkesh on 19/07/20.
 */
@Dao
interface LocationDao {

    @Query("SELECT * FROM userlocation Order by track_time desc")
    fun getAllLocation(): LiveData<List<UserLocation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(location: UserLocation)


}