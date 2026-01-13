package com.example.pag_baul

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StationAdapter(
    private val stations: List<String>,
    private val onStationClickListener: OnStationClickListener
) : RecyclerView.Adapter<StationAdapter.StationViewHolder>() {

    // Interface to handle click events on each station item
    interface OnStationClickListener {
        fun onStationClick(stationNumber: Int)
    }

    // Creates a new ViewHolder instance when the RecyclerView needs one.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return StationViewHolder(view)
    }

    // Binds the data to the views in the ViewHolder for a specific position.
    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        holder.stationName.text = stations[position]
        holder.itemView.setOnClickListener {
            // The station number is position + 1
            onStationClickListener.onStationClick(position + 1)
        }
    }

    // Returns the total number of items in the list.
    override fun getItemCount() = stations.size

    // ViewHolder class that holds the views for each item in the list.
    class StationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stationName: TextView = itemView.findViewById(android.R.id.text1)
    }
}
