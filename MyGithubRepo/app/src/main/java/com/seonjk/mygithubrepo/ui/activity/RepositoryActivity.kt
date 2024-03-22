package com.seonjk.mygithubrepo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.seonjk.mygithubrepo.R
import com.seonjk.mygithubrepo.data.database.GithubRepoEntity
import com.seonjk.mygithubrepo.databinding.ActivityRepositoryBinding
import com.seonjk.mygithubrepo.extensions.loadCenterInside
import com.seonjk.mygithubrepo.utility.DataBaseProvider
import com.seonjk.mygithubrepo.utility.RetrofitUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class RepositoryActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var repositoryJob: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + repositoryJob

    companion object {
        const val REPOSITORY_OWNER_KEY = "repositoryOwner"
        const val REPOSITORY_NAME_KEY = "repositoryName"
    }

    private lateinit var binding: ActivityRepositoryBinding

    private val repositoryDao by lazy { DataBaseProvider.provideDB(this).repositoryDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repositoryJob = Job()
        binding = ActivityRepositoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repositoryOwner = intent.getStringExtra(REPOSITORY_OWNER_KEY) ?: kotlin.run {
            Toast.makeText(this, "RepositoryOwner 이름이 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        val repositoryName = intent.getStringExtra(REPOSITORY_NAME_KEY) ?: kotlin.run {
            Toast.makeText(this, "Repository 이름이 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        launch {
            loadRepository(repositoryOwner, repositoryName)?.let { repo ->
                setData(repo)
            } ?: run {
                Toast.makeText(
                    this@RepositoryActivity,
                    "Repository 정보가 없습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private suspend fun loadRepository(
        repositoryOwner: String,
        repositoryName: String
    ): GithubRepoEntity? =
        withContext(coroutineContext) {
            var repository: GithubRepoEntity? = null
            withContext(Dispatchers.IO) {
                val response = RetrofitUtil.githubApiService.getRepository(
                    ownerLogin = repositoryOwner,
                    repoName = repositoryName
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    withContext(Dispatchers.Main) {
                        body?.let { repo ->
                            repository = repo
                        }
                    }
                }
            }
            repository
        }

    private fun setData(githubRepoEntity: GithubRepoEntity) = with(binding) {
        Log.e("data", githubRepoEntity.toString())
        showLoading(false)
        ownerProfileImageView.loadCenterInside(githubRepoEntity.owner.avatarUrl, 42f)
        ownerNameAndRepoNameTextView.text = "${githubRepoEntity.owner.login}/${githubRepoEntity.name}"
        stargazersCountText.text = githubRepoEntity.stargazersCount.toString()
        githubRepoEntity.language?.let { language ->
            languageText.isVisible = true
            languageText.text = language
        }?: kotlin.run {
            languageText.isGone = true
            languageText.text = ""
        }
        descriptionTextView.text = githubRepoEntity.description
        updateTimeTextView.text = githubRepoEntity.updatedAt

        setLikeState(githubRepoEntity)
    }

    private fun setLikeState(githubRepoEntity: GithubRepoEntity) = launch {
        withContext(Dispatchers.IO) {
            val repository = repositoryDao.getRepository(githubRepoEntity.fullName)
            val isLike = repository != null
            withContext(Dispatchers.Main) {
                setLikeImage(isLike)
                binding.likeButton.setOnClickListener {
                    likeRepository(githubRepoEntity, isLike)
                }
            }
        }
    }

    private fun setLikeImage(isLike: Boolean) {
        binding.likeButton.setImageDrawable(ContextCompat.getDrawable(this@RepositoryActivity,
            if (isLike) {
                R.drawable.ic_like
            } else {
                R.drawable.ic_dislike
            }
        ))
    }

    private fun likeRepository(githubRepoEntity: GithubRepoEntity, isLike: Boolean) = launch {
        withContext(Dispatchers.IO) {
            if (isLike) {
                repositoryDao.remove(githubRepoEntity.fullName)
            } else {
                repositoryDao.insert(githubRepoEntity)
            }
            withContext(Dispatchers.Main) {
                setLikeImage(isLike.not())
            }
        }
    }

    private fun showLoading(isShown: Boolean) = with(binding) {
        progressBar.isVisible = isShown
    }
}