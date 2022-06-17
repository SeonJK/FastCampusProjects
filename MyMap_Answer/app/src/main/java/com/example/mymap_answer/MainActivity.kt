package com.example.mymap_answer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.mymap_answer.databinding.ActivityMainBinding
import com.example.mymap_answer.model.LocationLatLngEntity
import com.example.mymap_answer.model.SearchResultEntity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SearchRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        initViews()

        initData()
        setData()
    }

    private fun initAdapter() {
        adapter = SearchRecyclerAdapter()
    }

    private fun initViews() = with(binding) {
        emptyTextView.isVisible = false
        recyclerView.adapter = adapter
    }

    private fun initData() {
        adapter.notifyDataSetChanged()
    }

    private fun setData() {
        val dataList = (0 .. 10).map {
            SearchResultEntity(
                name = "빌딩 $it",
                fullAddress = "주소 $it",
                locationLatLng = LocationLatLngEntity(
                    it.toFloat(),
                    it.toFloat()
                )
            )
        }
        adapter.setSearchResultList(dataList) {
            Toast.makeText(this,
                "빌딩이름: ${it.name}\n주소: ${it.fullAddress}\n위도/경도: ${it.locationLatLng}",
                Toast.LENGTH_SHORT)
                .show()

            startActivity(Intent(this, MapActivity::class.java))
        }
    }
}