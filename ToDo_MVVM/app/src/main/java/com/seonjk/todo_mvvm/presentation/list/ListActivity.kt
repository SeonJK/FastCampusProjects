package com.seonjk.todo_mvvm.presentation.list

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.seonjk.todo_mvvm.databinding.ActivityListBinding
import com.seonjk.todo_mvvm.presentation.BaseActivity
import com.seonjk.todo_mvvm.presentation.view.TaskListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
internal class ListActivity : BaseActivity<ListViewModel>(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private lateinit var binding: ActivityListBinding

    override val viewModel: ListViewModel by viewModels()

    @Inject lateinit var adapter: TaskListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun observeData() {
        viewModel.taskList.observe(this) { taskListState ->
            when(taskListState) {
                is TaskListState.UnInitialized -> initViews(binding)
                is TaskListState.Loading -> handleLoadingState()
                is TaskListState.Success -> handleSuccessState(taskListState)
                is TaskListState.Error -> { /* do Nothing */ }
            }
        }
    }

    private fun initViews(binding: ActivityListBinding) = with(binding) {
        taskList.layoutManager = LinearLayoutManager(this@ListActivity, LinearLayoutManager.VERTICAL, false)
        taskList.adapter = adapter


    }

    private fun handleLoadingState() {
        TODO("Not yet implemented")
    }

    private fun handleSuccessState(taskListState: TaskListState.Success) {
        TODO("Not yet implemented")
    }
}