package com.seonjk.mygithubrepo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.seonjk.mygithubrepo.data.database.GithubOwner
import com.seonjk.mygithubrepo.data.database.GithubRepoEntity
import com.seonjk.mygithubrepo.databinding.ActivityMainBinding
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

    private val repositoryDao by lazy { DataBaseProvider.provideDB(applicationContext).repositoryDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainJob = Job()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        launch {
            addmockData()
            val githubRepos = loadGithubRepos()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainJob.cancel()
    }

    private suspend fun addmockData() = withContext(Dispatchers.IO) {
        val mockData = (0 until 10).map {num ->
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