package com.example.todaywisesaying

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * TodayWiseSaying
 * Created by authorName
 * Date: 2022-04-22
 * Time: 오후 5:27
 * */
class QuotesPagerAdapter : RecyclerView.Adapter<QuotesPagerAdapter.QuotesViewHolder>() {

    inner class QuotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_quote,
            parent,
            false
        )
        return QuotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuotesViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}