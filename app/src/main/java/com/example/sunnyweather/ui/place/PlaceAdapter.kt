package com.example.sunnyweather.ui.place

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.R
import com.example.sunnyweather.logic.model.Geocode
import com.example.sunnyweather.logic.model.PlaceResponse
import com.example.sunnyweather.logic.model.WeatherResponse

class PlaceAdapter(private val _placeList:List<Geocode>): RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
//    private var _placeList= listOf<Geocode>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeName)
        val placeAddress: TextView = view.findViewById(R.id.placeAddress)
        val placeAdcode:TextView=view.findViewById(R.id.placeAdcode)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.place_item,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place=_placeList[position]
        holder.placeName.text=place.city
        holder.placeAddress.text=place.address
        holder.placeAdcode.text=place.adcode
    }



    override fun getItemCount(): Int {
        return _placeList.size
    }


}