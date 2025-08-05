package com.example.sunnyweather

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sunnyweather.databinding.FragmentPlaceBinding
import com.example.sunnyweather.logic.model.Geocode
import com.example.sunnyweather.ui.place.PlaceAdapter
import com.example.sunnyweather.ui.place.PlaceViewModel




class PlaceFragment : Fragment() {
    private lateinit var binding: FragmentPlaceBinding
    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }
    private lateinit var adapter: PlaceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.isPlaceSaved()){
            val place=viewModel.getSavedPlace()
            val intent=Intent(context,WeatherActivity::class.java).apply {
                putExtra("location", place.location)
                putExtra("place_name", place.address)
            }
            startActivity(intent)
            activity?.finish()
        }

        val layoutManager=LinearLayoutManager(activity)
        binding.recyclerView.layoutManager=layoutManager
        adapter=PlaceAdapter(this,viewModel.placeList)
        binding.recyclerView.adapter=adapter
        binding.searchPlaceEdit.addTextChangedListener(watcher)
        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result->
            val places=result.getOrNull()
            if (places!=null){
                binding.recyclerView.visibility=View.VISIBLE
                binding.bgImageView.visibility=View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places as Collection<Geocode>)
                adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(activity,"未能查询到任何地点",Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val context=binding.searchPlaceEdit.text.toString().trim()
            if(context.isNotEmpty()){
                viewModel.searchPlaces(context)
            }else{
                binding.recyclerView.visibility=View.GONE
                binding.bgImageView.visibility=View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }



        override fun afterTextChanged(s: Editable?) {}

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentPlaceBinding.inflate(layoutInflater)
        return binding.root
    }

}