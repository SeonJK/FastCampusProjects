package com.example.bookreview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookreview.model.Book
import com.example.bookreview.databinding.ItemBookBinding

/**
 * BookReview
 * Created by SeonJK
 * Date: 2022-05-09
 * Time: 오후 3:02
 * */
class BookListAdapter(private val itemClickedListener: (Book) -> Unit) :
    ListAdapter<Book, BookListAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(
        private val binding: ItemBookBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookModel: Book) {
            binding.titleText.text = bookModel.title
            binding.descriptionText.text = bookModel.description

            binding.root.setOnClickListener {
                itemClickedListener(bookModel)
            }

            Glide.with(binding.bookImage.context)
                .load(bookModel.imgUrl)
                .into(binding.bookImage)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(ItemBookBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}