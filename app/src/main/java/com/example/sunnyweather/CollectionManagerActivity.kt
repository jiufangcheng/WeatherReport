package com.example.sunnyweather

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sunnyweather.databinding.ActivityCollectionManagerBinding
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.enity.Place

import com.example.sunnyweather.ui.PlaceManager.PlaceManagerAdapter
import com.example.sunnyweather.ui.PlaceManager.PlaceManagerViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CollectionManagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCollectionManagerBinding
    private lateinit var viewModel:PlaceManagerViewModel
    private lateinit var adapter: PlaceManagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityCollectionManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel=ViewModelProvider(this).get(PlaceManagerViewModel::class.java)
        val layoutManager= LinearLayoutManager(this)
        binding.recyclerView.layoutManager=layoutManager
        adapter= PlaceManagerAdapter(this)
        binding.recyclerView.adapter=adapter

        viewModel.places.observe(this) { places ->
            adapter.setPlaces(places ?: emptyList())
        }

        // 观察搜索结果
        viewModel.searchResults.observe(this) { results ->
            adapter.setPlaces(results)
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchPlaces(newText?.trim() ?: "")
                return true
            }
        })

        binding.addCityButton.setOnClickListener {
            val intent= Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

    }

}




