package com.seonjk.todo_mvvm_koin.presentation

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Job

internal abstract class BaseActivity<VM: BaseViewModel> : AppCompatActivity() {

    abstract val viewModel: VM

    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        job = viewModel.fetchData()
        observeData()
    }

    abstract fun observeData()

    override fun onDestroy() {
        if (job.isActive) {
            job.cancel()
        }
        super.onDestroy()
    }
}