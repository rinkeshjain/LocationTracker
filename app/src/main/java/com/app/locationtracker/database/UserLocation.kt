package com.app.locationtracker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Rinkesh on 19/07/20.
 */
@Entity
data class UserLocation(
    @PrimaryKey(autoGenerate = true) val uid: Int=0,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "track_time") val trackTime: Long?,
    @ColumnInfo(name = "latitude") val latitude: String?,
    @ColumnInfo(name = "longitude") val longitude: String?
)
