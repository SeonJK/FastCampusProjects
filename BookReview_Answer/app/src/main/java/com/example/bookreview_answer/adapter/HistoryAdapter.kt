package com.example.bookreview_answer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookreview_answer.databinding.ItemHistoryBinding
import com.example.bookreview_answer.model.History

/**
 * BookReview_Answer
 * Created by SeonJK
 * Date: 2022-05-17
 * Time: 오후 5:21
 * */
class HistoryAdapter(val historyDeleteClickedListener: (String) -> Unit) :
    ListAdapter<History, HistoryAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(
        private val binding: ItemHistoryBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(keywords: History) {
            binding.historyKeywordTextView.text = keywords.keyword

            binding.historyKeywordDeleteButton.setOnClickListener {
                historyDeleteClickedListener(keywords.keyword.orEmpty())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.keyword == newItem.keyword
            }

        }
    }
}