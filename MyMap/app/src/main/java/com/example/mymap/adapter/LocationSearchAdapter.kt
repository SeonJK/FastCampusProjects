package com.example.mymap.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mymap.databinding.ItemLocationBinding
import com.example.mymap.model.SearchResult

/**
 * MyMap
 * Created by SeonJK
 * Date: 2022-05-23
 * Time: 오후 5:46
 * */
class LocationSearchAdapter :
    ListAdapter<SearchResult, LocationSearchAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(
        private val binding: ItemLocationBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: SearchResult) {
            binding.buildingTextView.text = result.buildingName
            binding.locationTextView.text = result.fullAddress
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(ItemLocationBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<SearchResult>() {
            override fun areItemsTheSame(
                oldItem: SearchResult,
                newItem: SearchResult,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: SearchResult,
                newItem: SearchResult,
            ): Boolean {
                return oldItem.fullAddress == newItem.fullAddress
            }

        }

    }
}