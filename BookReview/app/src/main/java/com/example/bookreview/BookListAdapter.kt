package com.example.bookreview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

/**
 * BookReview
 * Created by SeonJK
 * Date: 2022-05-09
 * Time: 오후 3:02
 * */
class BookListAdapter(
    private val context: Context,
    private val dataset: List<Book>?,
) : RecyclerView.Adapter<BookListAdapter.ViewHolder>() {

    class ViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        val bookImage: ImageView
        val titleText: TextView
        val descriptionText: TextView

        init {
            bookImage = itemView.findViewById(R.id.book_image)
            titleText = itemView.findViewById(R.id.title_text)
            descriptionText = itemView.findViewById(R.id.description_text)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.titleText.text = dataset!![position].title
        holder.descriptionText.text = dataset[position].description

        Glide.with(context)
            .load(dataset[position].imgUrl)
            .into(holder.bookImage)
    }

    override fun getItemCount(): Int {
        return dataset!!.size
    }
}