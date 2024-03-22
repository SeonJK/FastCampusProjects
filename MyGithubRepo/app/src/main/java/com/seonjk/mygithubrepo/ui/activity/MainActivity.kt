package com.seonjk.mygithubrepo.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.seonjk.mygithubrepo.data.database.GithubOwner
import com.seonjk.mygithubrepo.data.database.GithubRepoEntity
import com.seonjk.mygithubrepo.databinding.ActivityMainBinding
import com.seonjk.mygithubrepo.databinding.ActivitySearchBinding
import com.seonjk.mygithubrepo.ui.activity.RepositoryActivity.Companion.REPOSITORY_NAME_KEY
import com.seonjk.mygithubrepo.ui.activity.RepositoryActivity.Companion.REPOSITORY_OWNER_KEY
import com.seonjk.mygithubrepo.ui.adapter.RepositoryListAdapter
import com.seonjk.mygithubrepo.utility.DataBaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var binding: ActivityMainBinding

    lateinit var mainJob: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + mainJob

    private val repositoryDao by lazy {
        DataBaseProvider.provideDB(applicationContext).repositoryDao()
    }

    private lateinit var adapter: RepositoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainJob = Job()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initAdapter()

//        launch {
//            addmockData()
//            val githubRepos = loadGithubRepos()
//        }
    }

    override fun onResume() {
        super.onResume()
        launch(coroutineContext) {
            loadLikedRepositoryList()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainJob.cancel()
    }

    private fun initViews() = with(binding) {
        searchButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
        }
    }

    private fun initAdapter() {

    }

    private suspend fun loadLikedRepositoryList() = withContext(Dispatchers.IO) {
        val repoList = DataBaseProvider.provideDB(this@MainActivity).repositoryDao().getHistory()
        withContext(Dispatchers.Main) {
            setData(repoList)
        }
    }

    private fun setData(repoList: List<GithubRepoEntity>) = with(binding) {
        if (repoList.isEmpty()) {
            emptyResultTextView.isVisible = true
            recyclerView.isGone = true
        } else {
            emptyResultTextView.isGone = true
            recyclerView.isVisible = true
            adapter.setSearchResultList(repoList) {
                startActivity(
                    Intent(this@MainActivity, RepositoryActivity::class.java).apply {
                        putExtra(REPOSITORY_OWNER_KEY, it.owner.login)
                        putExtra(REPOSITORY_NAME_KEY, it.name)
                    }
                )
            }
        }
    }

    private suspend fun addmockData() = withContext(Dispatchers.IO) {
        val mockData = (0 until 10).map { num ->
            GithubRepoEntity(
                name = "repo $num",
                fullName = "name $num",
                owner = GithubOwner(
                    login = "login",
                    avatarUrl = "avatarUrl"
                ),
                description = null,
                language = null,
                updatedAt = Date().toString(),
                stargazersCount = num
            )
        }
        DataBaseProvider.provideDB(applicationContext).repositoryDao().insertAll(mockData)
    }

    private suspend fun loadGithubRepos() = withContext(Dispatchers.IO) {
        return@withContext repositoryDao.getHistory()
    }
}