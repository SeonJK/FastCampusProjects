package com.example.mymap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymap.adapter.LocationSearchAdapter
import com.example.mymap.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: LocationSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        searchLocation()
    }

    private fun initRecyclerView() {
        adapter = LocationSearchAdapter()
        val layoutManager = LinearLayoutManager(this)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager
    }

    private fun searchLocation() {
        binding.searchButton.setOnClickListener {
            // TODO: Retrofit 통신
        }
    }
}
