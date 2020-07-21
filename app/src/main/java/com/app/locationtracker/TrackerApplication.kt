package com.app.locationtracker

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.app.locationtracker.service.LocationService


/**
 * Created by Rinkesh on 13/07/20.
 */
class TrackerApplication : Application(), LifecycleObserver {


    private var locationServiceBound = false

    // Provides location updates for while-in-use.
    private var foregroundOnlyLocationService: LocationService? = null
    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LocationService.LocalBinder
            foregroundOnlyLocationService = binder.service
            locationServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            foregroundOnlyLocationService = null
            locationServiceBound = false
        }
    }


    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }


    fun locationTrackStatusUpdate(isEnable: Boolean) {
        if (isEnable) {
            foregroundOnlyLocationService?.unsubscribeToLocationUpdates()
        } else {
            foregroundOnlyLocationService?.subscribeToLocationUpdates()
                ?: Log.d("TAG", "Service Not Bound")
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        Log.d("TRACKER_APPLICATION", "ON START")
        val serviceIntent = Intent(this, LocationService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() {
        // app moved to background
        Log.d("TRACKER_APPLICATION", "ON STOP")
        if (locationServiceBound) {
            unbindService(foregroundOnlyServiceConnection)
            locationServiceBound = false
        }
    }

}