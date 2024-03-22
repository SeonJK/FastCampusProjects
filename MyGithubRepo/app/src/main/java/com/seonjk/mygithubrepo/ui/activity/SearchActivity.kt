package com.seonjk.mygithubrepo.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isGone
import com.seonjk.mygithubrepo.data.database.GithubRepoEntity
import com.seonjk.mygithubrepo.data.response.GithubRepoSearchResponse
import com.seonjk.mygithubrepo.databinding.ActivitySearchBinding
import com.seonjk.mygithubrepo.ui.activity.RepositoryActivity.Companion.REPOSITORY_NAME_KEY
import com.seonjk.mygithubrepo.ui.activity.RepositoryActivity.Companion.REPOSITORY_OWNER_KEY
import com.seonjk.mygithubrepo.ui.adapter.RepositoryListAdapter
import com.seonjk.mygithubrepo.utility.RetrofitUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SearchActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var searchJob: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + searchJob

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: RepositoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchJob = Job()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        initViews()
        bindViews()
    }

    private fun initAdapter() = with(binding) {
        adapter = RepositoryListAdapter()
    }

    private fun initViews() = with(binding) {
        emptyResultTextView.isGone = true
        recyclerView.adapter = adapter
    }

    private fun bindViews() = with(binding) {
        searchButton.setOnClickListener {  }
    }

    private fun searchKeyword(keyword: String) = launch {
        withContext(Dispatchers.IO) {
            val response = RetrofitUtil.githubApiService.searchRepositories(keyword)
            if (response.isSuccessful) {
                val body = response.body()
                withContext(Dispatchers.Main) {
                    body?.let {searchResponse ->
                        setData(searchResponse.items)
                    }
                }
            }
        }
    }

    private fun setData(items: List<GithubRepoEntity>) = with(binding) {
        adapter.setSearchResultList(items) {
            startActivity(
                Intent(this@SearchActivity, RepositoryActivity::class.java).apply {
                    putExtra(REPOSITORY_OWNER_KEY, it.owner.login)
                    putExtra(REPOSITORY_NAME_KEY, it.name)
                }
            )
        }
    }
}
