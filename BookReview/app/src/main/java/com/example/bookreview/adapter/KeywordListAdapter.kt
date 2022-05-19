package com.example.bookreview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookreview.databinding.HistoryRowBinding
import com.example.bookreview.model.History

/**
 * BookReview
 * Created by SeonJK
 * Date: 2022-05-13
 * Time: 오후 5:15
 * */
class KeywordListAdapter(
    val historyDeleteClickedListener: (String) -> Unit,
    val historyClickedListener: (String) -> Unit
) : ListAdapter<History, KeywordListAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(
        private val binding: HistoryRowBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(keywords: History) {
            binding.keywordTextView.text = keywords.keyword
            binding.keywordTextView.setOnClickListener {
                // 키워드를 눌렀을 때 해당 검색어를 검색할 수 있는 기능 구현
                historyClickedListener(keywords.keyword.orEmpty())
            }

            binding.clearButton.setOnClickListener {
                historyDeleteClickedListener(keywords.keyword.orEmpty())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(HistoryRowBinding.inflate(LayoutInflater.from(parent.context)))
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
                return oldItem.uid == newItem.uid
            }

        }
    }
}