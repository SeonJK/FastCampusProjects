package com.example.bookreview_answer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.bookreview_answer.adapter.BookAdapter
import com.example.bookreview_answer.adapter.HistoryAdapter
import com.example.bookreview_answer.api.BookService
import com.example.bookreview_answer.databinding.ActivityMainBinding
import com.example.bookreview_answer.model.BestSellerDto
import com.example.bookreview_answer.model.History
import com.example.bookreview_answer.model.SearchBookDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    val TAG: String = "로그"

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var bookService: BookService
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclerView()
        initHistoryRecyclerView()

        db = getAppDatabase(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bookService = retrofit.create(BookService::class.java)

        bookService.getBestSellerBooks(getString(R.string.interpark_API_key))
            .enqueue(object : Callback<BestSellerDto> {
                override fun onResponse(
                    call: Call<BestSellerDto>,
                    response: Response<BestSellerDto>,
                ) {
                    if (response.isSuccessful.not()) {
                        // 실패에 대한 예외처리
                        Log.d(TAG, "onResponse() called :: NOT SUCCESS!!")
                        return
                    }

                    response.body()?.let {
                        Log.d(TAG, it.toString())

                        it.books.forEach { book ->
                            Log.d(TAG, book.toString())
                        }

                        adapter.submitList(it.books)
                    }
                }

                override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {
                    Log.d(TAG, "apiCallback - onFailure() called :: ${t.message.toString()}")
                }

            })
    }

    private fun initBookRecyclerView() {
        adapter = BookAdapter {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("bookModel", it)
            startActivity(intent)
        }

        binding.bookRecyclerView.adapter = adapter
        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun initHistoryRecyclerView() {
        historyAdapter = HistoryAdapter { deleteKeyword(it) }

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historyAdapter

        initSearchEditText()
    }

    private fun initSearchEditText() {
        binding.searchEditText.setOnKeyListener { _, keyCode, event ->
            if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == MotionEvent.ACTION_DOWN)) {
                search(binding.searchEditText.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        binding.searchEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                showHistoryView()
            }
            return@setOnTouchListener false
        }
    }

    private fun showHistoryView() {
        Thread {
            val keywords = db.historyDao().getAllHistories().reversed()

            runOnUiThread {
                binding.historyRecyclerView.visibility = View.VISIBLE
                historyAdapter.submitList(keywords)
            }
        }.start()
    }

    private fun hideHistoryView() {
        binding.historyRecyclerView.visibility = View.GONE
    }

    private fun search(keyword: String) {
        bookService.getBooksByName(getString(R.string.interpark_API_key), keyword)
            .enqueue(object : Callback<SearchBookDto> {
                override fun onResponse(
                    call: Call<SearchBookDto>,
                    response: Response<SearchBookDto>,
                ) {
                    if (response.isSuccessful.not()) {
                        // 실패에 대한 예외처리
                        Log.d(TAG, "onResponse() called :: NOT SUCCESS!!")
                        return
                    }

                    adapter.submitList(response.body()?.books.orEmpty())

                    hideHistoryView()
                    saveSearchKeyword(keyword)
                    }

                override fun onFailure(call: Call<SearchBookDto>, t: Throwable) {
                    hideHistoryView()
                    Log.d(TAG, "apiCallback - onFailure() called :: ${t.message.toString()}")
                }
            })
    }

    private fun saveSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().insertHistory(History(null, keyword))
        }.start()
    }

    private fun deleteKeyword(keyword: String) {
        Thread {
            db.historyDao().deleteHistory(keyword)
            showHistoryView()
        }.start()
    }
}