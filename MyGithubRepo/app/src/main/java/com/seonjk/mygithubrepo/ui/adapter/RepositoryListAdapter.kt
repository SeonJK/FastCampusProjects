package com.seonjk.mygithubrepo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.seonjk.mygithubrepo.data.database.GithubRepoEntity
import com.seonjk.mygithubrepo.databinding.ItemRepositoryBinding
import com.seonjk.mygithubrepo.extensions.loadCenterInside

class RepositoryListAdapter :
    RecyclerView.Adapter<RepositoryListAdapter.RepositoryItemViewHolder>() {

    private var repositoryList: List<GithubRepoEntity> = listOf()
    private lateinit var repositoryClickListener: (GithubRepoEntity) -> Unit

    inner class RepositoryItemViewHolder(
        private val binding: ItemRepositoryBinding,
        val itemClickListener: (GithubRepoEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(data: GithubRepoEntity) = with(binding) {
            ownerProfileImageView.loadCenterInside(data.owner.avatarUrl, 24f)
            ownerNameTextView.text = data.owner.login
            nameTextView.text = data.fullName
            subtextTextView.text = data.description
            stargazersCountText.text = data.stargazersCount.toString()
            data.language?.let { language ->
                languageText.isVisible = true
                languageText.text = language
            } ?: kotlin.run {
                languageText.isGone = true
                languageText.text = ""
            }
        }

        fun bindViews(data: GithubRepoEntity) {
            binding.root.setOnClickListener {
                itemClickListener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryItemViewHolder {
        val view = ItemRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepositoryItemViewHolder(view, repositoryClickListener)
    }

    override fun getItemCount(): Int = repositoryList.size

    override fun onBindViewHolder(holder: RepositoryItemViewHolder, position: Int) {
        holder.bindData(repositoryList[position])
        holder.bindViews(repositoryList[position])
    }

    fun setSearchResultList(searchResultList: List<GithubRepoEntity>, searchResultClickListener: (GithubRepoEntity) -> Unit) {
        this.repositoryList = searchResultList
        this.repositoryClickListener = searchResultClickListener
        notifyDataSetChanged()
    }
}