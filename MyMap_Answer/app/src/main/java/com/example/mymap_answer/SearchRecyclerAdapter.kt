package com.example.mymap_answer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mymap_answer.databinding.ViewholderSearchResultItemBinding

/**
 * MyMap_Answer
 * Created by SeonJK
 * Date: 2022-05-26
 * Time: 오전 9:37
 * */
class SearchRecyclerAdapter :
    RecyclerView.Adapter<SearchRecyclerAdapter.SearchResultItemViewHolder>() {
    private var searchResultList: List<Any> = listOf()
    lateinit var searchResultClickListener: (Any) -> Unit

    class SearchResultItemViewHolder(
        val binding: ViewholderSearchResultItemBinding,
        val searchResultClickListener: (Any) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: Any) = with(binding) {
            titleTextView.text = "제목"
            subTextView.text = "부제목"

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchResultItemViewHolder {
        val view = ViewholderSearchResultItemBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return SearchResultItemViewHolder(view, searchResultClickListener)
    }

    override fun onBindViewHolder(holder: SearchResultItemViewHolder, position: Int): Unit {
        holder.bindData(searchResultList[position])
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}