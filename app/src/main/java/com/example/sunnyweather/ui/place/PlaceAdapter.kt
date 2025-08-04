package com.example.sunnyweather.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.R
import com.example.sunnyweather.WeatherActivity
import com.example.sunnyweather.logic.model.Geocode

class PlaceAdapter(private  val fragment: Fragment,private val _placeList:List<Geocode>): RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
//    private var _placeList= listOf<Geocode>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeName)
        val placeAddress: TextView = view.findViewById(R.id.placeAddress)
        val placeAdcode:TextView=view.findViewById(R.id.placeAdcode)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.place_item,parent,false)
        val holder= ViewHolder(v)
        holder.itemView.setOnClickListener{
            val position=holder.adapterPosition
            val place=_placeList[position]
            val intent=Intent(parent.context,WeatherActivity::class.java).apply {
                putExtra("location", place.location)
                putExtra("place_name", place.address)
            }
            fragment.startActivity(intent)
            fragment.activity?.finish()
        }
        return holder
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