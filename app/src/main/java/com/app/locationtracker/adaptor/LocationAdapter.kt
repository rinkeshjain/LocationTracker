package com.app.locationtracker.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.locationtracker.R
import com.app.locationtracker.database.UserLocation
import com.app.locationtracker.util.toDate

class LocationAdapter internal constructor(
    val context: Context
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {


    private var locations = emptyList<UserLocation>() // Cached copy of words

    inner class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val latitudeValue: TextView = itemView.findViewById(R.id.latValue)
        val longitudeValue: TextView = itemView.findViewById(R.id.longiValue)
        val timeStamp: TextView = itemView.findViewById(R.id.dateCaption)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.recyclerview_item, parent, false)
        return LocationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val current = locations[position]
        holder.latitudeValue.text = current.latitude
        holder.longitudeValue.text = current.longitude
        holder.timeStamp.text = current.trackTime?.toDate()
    }

    internal fun setLocation(words: List<UserLocation>) {
        this.locations = words
        notifyDataSetChanged()
    }

    override fun getItemCount() = locations.size
}