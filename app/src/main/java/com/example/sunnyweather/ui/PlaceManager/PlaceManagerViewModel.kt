package com.example.sunnyweather.ui.PlaceManager

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.database.PlaceDatabase
import com.example.sunnyweather.logic.enity.Place
import com.example.sunnyweather.logic.model.Geocode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class PlaceManagerViewModel(application: Application) : AndroidViewModel(application) {
    private val _places = MutableLiveData<List<Place>>()
    val places=_places.switchMap {
        list->
        Repository.allPlaces
    }

    init {
        loadAllPlaces()
    }

    // 搜索功能
    private val _searchQuery = MutableLiveData<String>()
    val searchResults: LiveData<List<Place>> = _searchQuery.switchMap { query ->
        liveData(Dispatchers.IO) {
            emit(
                if (query.isBlank()) {
                    Repository.allPlaces.value ?: emptyList() // 空搜索时返回全量
                } else {
                    Repository.queryPlaces(query) // 非空时返回搜索结果
                }
            )
        }
    }



    // 触发搜索
    fun searchPlaces(query: String) {
        _searchQuery.value = query
    }

    // 添加新城市
    fun addPlace(address: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result= Repository.searchPlace(address).value
                val places=result?.getOrNull()
                if (places!=null) {
                    val geocode=places[0]

                    if (Repository.queryPlace(geocode.address)!=null){
                        val place= Place(name = geocode.address, location = geocode.location)
                        Repository.addPlace(place)
                        Result.success(place)
                    }else{
                        Result.failure<Place>(RuntimeException("已经存在"))
                    }

                }else{
                    Result.failure<Place>(RuntimeException("加入失败"))
            }
        }
    }

    // 删除城市
    fun deletePlace(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
            Repository.deletePlace(place)
        }
    }

    fun loadAllPlaces() {
        viewModelScope.launch {
            _places.value = Repository.allPlaces.value
        }
    }
}