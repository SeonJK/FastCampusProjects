package com.example.bookreview

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookreview.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    val TAG: String = "로그"

    companion object {
        private const val API_KEY: String =
            "47526898744FF03FF8113BD501F34FB4E2E19D7BB8076833B2D3F2F1442C8C23"
        private const val OUTPUT: String = "json"
        private const val CATEGORY_ID: Int = 100
    }

    object RetrofitBuilder {
        var service: RetrofitService

        init {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://book.interpark.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            service = retrofit.create(RetrofitService::class.java)
        }
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // enqueue로 비동기 통신을 실행
        RetrofitBuilder.service.getBestSellerList(API_KEY, CATEGORY_ID, OUTPUT)
            .enqueue(object : Callback<BookItem> {
                override fun onResponse(call: Call<BookItem>, response: Response<BookItem>) {
                    val books = response.body()
                    val bookList = books?.bookList

                    val adapter = BookListAdapter(this@MainActivity, bookList)
                    val layoutManager =
                        LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)

                    binding.recyclerView.layoutManager = layoutManager
                    binding.recyclerView.adapter = adapter
                }

                override fun onFailure(call: Call<BookItem>, t: Throwable) {
                    Log.d(TAG, "apiCallback - onFailure() called :: ${t.message.toString()}")
                }

            })
    }
}