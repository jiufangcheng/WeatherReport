package com.example.sunnyweather.ui.add

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RestrictTo
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.AddActivity
import com.example.sunnyweather.R
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.enity.Place
import com.example.sunnyweather.logic.model.Geocode
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AddPlaceAdapter(private val addActivity: AddActivity,private val _placeList:List<Geocode>):
    RecyclerView.Adapter<AddPlaceAdapter.ViewHolder> (){
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeName)
        val placeAddress: TextView = view.findViewById(R.id.placeAddress)
        val placeAdcode:TextView=view.findViewById(R.id.placeAdcode)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.add_item,parent,false)

        val holder= ViewHolder(v)
        holder.itemView.setOnClickListener {
            val position=holder.adapterPosition
            val place=_placeList[position]
            val activity=addActivity
            val newPlace= Place(name = place.address, location = place.location)
            runBlocking {
                val result = async {
                    Repository.addPlace(newPlace)
                }.await()
                if (result==null){
                    Toast.makeText(activity,"添加失败", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(activity,"添加成功", Toast.LENGTH_SHORT).show()
                }
            }

        }

        return holder
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val place=_placeList[position]
        holder.placeName.text=place.province
        holder.placeAddress.text=place.address
        holder.placeAdcode.text=place.adcode
    }

    override fun getItemCount(): Int {
        return _placeList.size
    }


}