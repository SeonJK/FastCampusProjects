package com.seonjk.mygithubrepo.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import com.seonjk.mygithubrepo.BuildConfig
import com.seonjk.mygithubrepo.databinding.ActivitySigninBinding
import com.seonjk.mygithubrepo.utility.AuthTokenProvider
import com.seonjk.mygithubrepo.utility.RetrofitUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SignInActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var binding: ActivitySigninBinding

    private val authTokenProvider by lazy { AuthTokenProvider(this) }

    lateinit var signInJob: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + signInJob

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInJob = Job()
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (authTokenProvider.token.isNullOrEmpty().not()) {
            startMainActivity()
        } else {
            initViews()
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        signInJob.cancel()
    }

    private fun initViews() = with(binding) {
        loginButton.setOnClickListener {
            loginGithub()
        }
    }

    private fun loginGithub() {
        /** https://github.com/login/oauth/authorize?client_id=${github_client_id} */
        val loginUrl = Uri.Builder().scheme("https").authority("github.com")
            .appendPath("login")
            .appendPath("oauth")
            .appendPath("authorize")
            .appendQueryParameter("client_id", BuildConfig.GITHUB_CLIENT_ID)
            .build()

        // androidx.browser API
        CustomTabsIntent.Builder().build().also {
            it.launchUrl(this, loginUrl)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.data?.getQueryParameter("code")?.let { code ->
            launch(coroutineContext) {
                showProgress(true)
                getAccessToken(code)
                showProgress(false)

                if (authTokenProvider.token.isNullOrEmpty().not()) {
                    startMainActivity()
                }
            }
        }
    }

    private suspend fun showProgress(visible: Boolean) = withContext(coroutineContext) {
        with(binding) {
            loginButton.isVisible = !visible
            progressBar.isVisible = visible
            progressTextView.isVisible = visible
        }
    }

    private suspend fun getAccessToken(code: String) = withContext(Dispatchers.IO) {
        val response = RetrofitUtil.authApiService.getAccessToken(
            clientId = BuildConfig.GITHUB_CLIENT_ID,
            clientSecret = BuildConfig.GITHUB_CLIENT_SECRET,
            code = code
        )

        if (response.isSuccessful) {
            val accessToken = response.body()?.accessToken ?: ""
            Log.e("jkseon", "accessToken=$accessToken")
            if (accessToken.isNotEmpty()) {
                authTokenProvider.updateToken(accessToken)
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@SignInActivity,
                        "accessToken not exist.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Log.e("jkseon", "response Failed")
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@SignInActivity,
                    "Github Login Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}