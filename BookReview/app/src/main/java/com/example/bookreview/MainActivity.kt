package com.example.bookreview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.bookreview.adapter.BookListAdapter
import com.example.bookreview.adapter.KeywordListAdapter
import com.example.bookreview.database.AppDatabase
import com.example.bookreview.databinding.ActivityMainBinding
import com.example.bookreview.model.BookItem
import com.example.bookreview.model.History
import com.example.bookreview.service.RetrofitService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    val TAG: String = "로그"

    object RetrofitBuilder {
        var service: RetrofitService

        init {
            // setLenient() 속성을 통해 parser 허용을 자유롭게 설정
            val gson: Gson = GsonBuilder()
                .setLenient()
                .create()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://book.interpark.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            service = retrofit.create(RetrofitService::class.java)
        }
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var bookListAdapter: BookListAdapter
    private lateinit var keywordListAdapter: KeywordListAdapter
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        initKeywordListRecyclerView()
        initDatabase()

        getBestSellerList()
        editTextFocused()
    }

    private fun initRecyclerView() {
        bookListAdapter = BookListAdapter {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("bookModel", it)
            startActivity(intent)
        }
        val layoutManager = LinearLayoutManager(this)

        binding.bookListRecyclerView.layoutManager = layoutManager
        binding.bookListRecyclerView.adapter = bookListAdapter
    }

    private fun initKeywordListRecyclerView() {
        keywordListAdapter = KeywordListAdapter(
            historyDeleteClickedListener = {removeKeywordFromDB(it)},
            historyClickedListener = {searchPreviousKeyword(it)}
        )
        val layoutManager = LinearLayoutManager(this)

        binding.keywordRecyclerView.adapter = keywordListAdapter
        binding.keywordRecyclerView.layoutManager = layoutManager
    }

    private fun initDatabase() {
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "keywordHistoryDB"
        ).build()
    }

    private fun getBestSellerList() {
        // enqueue로 비동기 통신을 실행
        RetrofitBuilder.service.getBestSellerList(getString(R.string.api_key))
            .enqueue(object : Callback<BookItem> {
                override fun onResponse(call: Call<BookItem>, response: Response<BookItem>) {
                    val books = response.body()
                    val bookList = books?.bookList

                    bookListAdapter.submitList(bookList)
                }

                override fun onFailure(call: Call<BookItem>, t: Throwable) {
                    Log.d(TAG, "getBestSellerList - onFailure() called :: ${t.message.toString()}")
                }

            })
    }

    private fun editTextFocused() {
        binding.searchEditText.setOnKeyListener { _, keyCode, event ->
            if ((event.action == MotionEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // api 호출
                searchBookList()
                // 검색어 DB 저장
                saveKeywordDB(binding.searchEditText.text.toString())
                // 키보드 숨기기
                hideKeyboard()

                true
            } else {
                false
            }
        }

        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.keywordLayout.visibility = View.VISIBLE
                // 검색어 기록 바인딩
                bindKeywordList()
            } else {
                binding.keywordLayout.visibility = View.GONE
            }
        }

        // 닫기 버튼을 눌렀을 때
        closeEditTextButton()
    }

    private fun searchBookList() {
        RetrofitBuilder.service.searchBookList(getString(R.string.api_key), binding.searchEditText.text.toString())
            .enqueue(object : Callback<BookItem> {
                override fun onResponse(call: Call<BookItem>, response: Response<BookItem>) {
                    response.body()?.let {
                        if (response.isSuccessful.not()) {
                            Log.e(TAG, "NOT SUCCESS!!")
                            return
                        }

                        binding.keywordLayout.visibility = View.GONE
                        bookListAdapter.submitList(it.bookList)
                    }
                }

                override fun onFailure(call: Call<BookItem>, t: Throwable) {
                    binding.keywordLayout.visibility = View.GONE
                    Log.d(TAG, "searchBookList - onFailure() called :: ${t.message.toString()}")
                }
            })
    }

    private fun saveKeywordDB(keyword: String) {
        Thread(Runnable {
            db.historyDao().insertHistory(History(null, keyword))
        }).start()
    }

    private fun bindKeywordList() {
        Thread(Runnable {
            val keywordList = db.historyDao().getAllHistories().reversed()

            runOnUiThread {
                keywordListAdapter.submitList(keywordList)
            }
        }).start()
    }

    private fun hideKeyboard() {
        // 키보드 숨기기
        val imm: InputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }

    private fun closeEditTextButton() {
        binding.closeKeywordListButton.setOnClickListener {
            binding.searchEditText.clearFocus()
            binding.keywordLayout.visibility = View.GONE
            hideKeyboard()
        }
    }

    private fun removeKeywordFromDB(keyword: String) {
        Thread {
            // DB에서 해당 아이템 삭제
            db.historyDao().deleteHistory(keyword)
        }.start()

        // 레이아웃에 다시 그려주기 (리사이클러뷰 어댑터에 리스트 다시 제출)
        bindKeywordList()
    }

    private fun searchPreviousKeyword(keyword: String) {
        binding.searchEditText.setText(keyword)
        binding.searchEditText.clearFocus()

        searchBookList()
        hideKeyboard()

    }
}