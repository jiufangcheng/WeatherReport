package com.example.sunnyweather.ui.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.model.Geocode

class AddPlaceViewModel : ViewModel() {

    private val searchPlaceLiveData=MutableLiveData<String>()

    val placeList=ArrayList<Geocode>()

    val placeLiveData = searchPlaceLiveData.switchMap() { address ->
        Repository.searchPlace(address)
    }

    fun searchPlaces(address: String) {
        searchPlaceLiveData.value = address
    }



}