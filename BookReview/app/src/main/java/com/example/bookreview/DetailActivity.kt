package com.example.bookreview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.bookreview.database.AppDatabase
import com.example.bookreview.databinding.ActivityDetailBinding
import com.example.bookreview.model.Book
import com.example.bookreview.model.Review

class DetailActivity : AppCompatActivity() {
    val TAG: String = "로그"

    private lateinit var binding: ActivityDetailBinding

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initDatabase()
        val model = intent.getParcelableExtra<Book>("bookModel")

        bindViews(model)

        runSaveButton(model)
    }

    private fun initDatabase() {
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "keywordHistoryDB"
        ).build()
    }

    private fun bindViews(model: Book?) {
        binding.titleTextView.text = model?.title
        Log.d(TAG, "DetailActivity - bindViews() called :: titleTextView=${binding.titleTextView.text}")
        binding.descriptionTextView.text = model?.description.orEmpty()
        Log.d(TAG, "DetailActivity - bindViews() called :: descriptionTextView=${binding.descriptionTextView.text}")

        Glide.with(binding.bookImageView.context)
            .load(model?.imgUrl.orEmpty())
            .into(binding.bookImageView)

        Thread{
            val review: Review? = db.reviewDao().getReview(model?.id?.toInt() ?: 0)
            runOnUiThread {
                binding.reviewEditText.setText(review?.text.toString())
            }
        }.start()
    }

    private fun runSaveButton(model: Book?) {
        binding.saveButton.setOnClickListener {
            Thread{
                db.reviewDao().saveReview(
                    Review(
                        model?.id?.toInt() ?: 0,
                        binding.reviewEditText.text.toString()
                    )
                )
            }.start()
        }
    }

}