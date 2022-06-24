package com.example.mymap_answer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.mymap_answer.MapActivity.Companion.SEARCH_RESULT_EXTRA_KEY
import com.example.mymap_answer.databinding.ActivityMainBinding
import com.example.mymap_answer.model.LocationLatLngEntity
import com.example.mymap_answer.model.SearchResultEntity
import com.example.mymap_answer.response.search.Poi
import com.example.mymap_answer.response.search.Pois
import com.example.mymap_answer.utility.RetrofitUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        // 어느 스레드에서 돌아갈지 명시해줌 -> Main 스레드에서 동작
        get() = Dispatchers.Main + job

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SearchRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Job 객체 초기화
        job = Job()

        initAdapter()
        initViews()
        bindViews()

        initData()
//        setData()
    }

    private fun initAdapter() {
        adapter = SearchRecyclerAdapter()
    }

    private fun initViews() = with(binding) {
        emptyTextView.isVisible = false
        recyclerView.adapter = adapter
    }

    private fun bindViews() = with(binding) {
        searchButton.setOnClickListener {
            searchKeyword(searchEditText.text.toString())
        }
    }

    private fun initData() {
        adapter.notifyDataSetChanged()
    }

    private fun setData(pois: Pois) {
        // TODO: 목업 데이터임. API 데이터로 대체할 것
        val dataList = pois.poi.map {
            SearchResultEntity(
                name = it.name ?: "빌딜명 없음",
                fullAddress = makeMainAddress(it),
                locationLatLng = LocationLatLngEntity(
                    it.noorLat,
                    it.noorLon
                )
            )
        }


        adapter.setSearchResultList(dataList) {
            Toast.makeText(this,
                "빌딩이름: ${it.name}\n주소: ${it.fullAddress}\n위도/경도: ${it.locationLatLng}",
                Toast.LENGTH_SHORT).show()

            startActivity(
                Intent(this, MapActivity::class.java).apply {
                    putExtra(SEARCH_RESULT_EXTRA_KEY, it)
                }
            )
        }
    }

    private fun searchKeyword(keywordString: String) {
        launch(coroutineContext) {
            try {
                // I/O 스레드로 전환해서 API 데이터 가져오기
                withContext(Dispatchers.IO) {
                    val response = RetrofitUtil.apiService.getSearchLocation(
                        keyword = keywordString
                    )
                    if (response.isSuccessful) {
                        val body = response.body()
                        // Main 스레드로 전환
                        withContext(Dispatchers.Main) {
                            Log.e("response", body.toString())
                            body?.let {
                                setData(it.searchPoiInfo.pois)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun makeMainAddress(poi: Poi): String =
        if (poi.secondBuildNo?.trim().isNullOrEmpty()) {
            (poi.upperAddrName?.trim() ?: "") + " " +
                    (poi.middleAddrName?.trim() ?: "") + " " +
                    (poi.lowerAddrName?.trim() ?: "") + " " +
                    (poi.detailAddrName?.trim() ?: "") + " " +
                    poi.firstBuildNo?.trim()
        } else {
            (poi.upperAddrName?.trim() ?: "") + " " +
                    (poi.middleAddrName?.trim() ?: "") + " " +
                    (poi.lowerAddrName?.trim() ?: "") + " " +
                    (poi.detailAddrName?.trim() ?: "") + " " +
                    (poi.firstBuildNo?.trim() ?: "") + " " +
                    poi.secondBuildNo?.trim()
        }
}