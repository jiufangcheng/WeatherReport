package com.example.sunnyweather.ui.PlaceManager

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.CollectionManagerActivity
import com.example.sunnyweather.R
import com.example.sunnyweather.WeatherActivity
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.enity.Place
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking


class PlaceManagerAdapter(private  val activity: CollectionManagerActivity, private var places:List<Place> =emptyList()):RecyclerView.Adapter<PlaceManagerAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val collectionPlaceName: TextView = view.findViewById(R.id.collectionPlaceName)
        val favoriteButton: ImageView=view.findViewById(R.id.favoriteButton)
    }
    fun setPlaces(newPlaces: List<Place>) {
        val diff = DiffUtil.calculateDiff(PlaceDiffCallback(places, newPlaces))
        places = newPlaces
        diff.dispatchUpdatesTo(this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.collection_manager_item,parent,false)
        val holder= ViewHolder(v)

        holder.itemView.setOnClickListener{
            val position=holder.adapterPosition
            val place=places[position]
            val activity=activity

            val intent= Intent(parent.context,WeatherActivity::class.java).apply {
                putExtra("location", place.location)
                putExtra("place_name", place.name)
            }
            activity.startActivity(intent)
        }
        holder.favoriteButton.setOnClickListener {
            if (holder.favoriteButton.isSelected){
                holder.favoriteButton.isSelected=false
            }else{
                holder.favoriteButton.isSelected=true
            }
            val position=holder.adapterPosition
            val place=places[position]
            val activity=activity
            val isSelected=holder.favoriteButton.isSelected
            if (isSelected==false){
                runBlocking {
                    val result=async {
                        Repository.deletePlace(place)
                    }.await()
                    if (result==0){
                        Toast.makeText(activity,"取消失败", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(activity,"取消成功，请刷新", Toast.LENGTH_SHORT).show()
                    }
                }

            }else{
                runBlocking {
                    Repository.addPlace(place)
                }
            }

        }
        return holder
    }

    override fun getItemCount(): Int {
        return places.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place=places[position]
        holder.collectionPlaceName.text=place.name
        holder.favoriteButton.isSelected=true
    }
    private inner class PlaceDiffCallback(
        private val oldList: List<Place>,
        private val newList: List<Place>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldPos: Int, newPos: Int) =
            oldList[oldPos].id == newList[newPos].id
        override fun areContentsTheSame(oldPos: Int, newPos: Int) =
            oldList[oldPos] == newList[newPos]
    }
}