package com.example.sunnyweather

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sunnyweather.databinding.ActivityAddBinding
import com.example.sunnyweather.logic.model.Geocode
import com.example.sunnyweather.ui.add.AddPlaceAdapter
import com.example.sunnyweather.ui.add.AddPlaceViewModel

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    lateinit var viewModel: AddPlaceViewModel
    private lateinit var adapter: AddPlaceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(AddPlaceViewModel::class.java)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        adapter = AddPlaceAdapter(this, viewModel.placeList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager
        binding.searchPlaceEdit.addTextChangedListener(watcher)
        viewModel.placeLiveData.observe(this, Observer { result ->
            val places = result.getOrNull()
            if (places != null) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places as Collection<Geocode>)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }

    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val context = binding.searchPlaceEdit.text.toString().trim()
            if (context.isNotEmpty()) {
                viewModel.searchPlaces(context)
            } else {
                binding.recyclerView.visibility = View.GONE
                binding.bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }
}