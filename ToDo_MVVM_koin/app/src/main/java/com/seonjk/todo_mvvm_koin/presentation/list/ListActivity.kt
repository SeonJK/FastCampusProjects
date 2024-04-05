package com.seonjk.todo_mvvm_koin.presentation.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.seonjk.todo_mvvm_koin.R
import com.seonjk.todo_mvvm_koin.databinding.ActivityListBinding
import com.seonjk.todo_mvvm_koin.presentation.BaseActivity
import com.seonjk.todo_mvvm_koin.presentation.detail.DetailActivity
import com.seonjk.todo_mvvm_koin.presentation.detail.DetailMode
import com.seonjk.todo_mvvm_koin.presentation.view.TaskListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

internal class ListActivity : BaseActivity<ListViewModel>(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private lateinit var binding: ActivityListBinding

    override val viewModel: ListViewModel by viewModel()

    private lateinit var adapter: TaskListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun observeData() {
        viewModel.taskListLiveData.observe(this) { taskListState ->
            when (taskListState) {
                is TaskListState.Uninitialized -> initViews()
                is TaskListState.Loading -> handleLoadingState()
                is TaskListState.Success -> handleSuccessState(taskListState)
                is TaskListState.Error -> handleErrorState()
            }
        }
    }

    private fun initViews() = with(binding) {
        taskList.layoutManager =
            LinearLayoutManager(this@ListActivity, LinearLayoutManager.VERTICAL, false)
        taskList.adapter = adapter

        refreshLayout.setOnRefreshListener {
            viewModel.fetchData()
        }

        newTaskButton.setOnClickListener {
            startActivityForResult(
                DetailActivity.getIntent(this@ListActivity, DetailMode.WRITE),
                DetailActivity.FETCH_REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DetailActivity.FETCH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            viewModel.fetchData()
        }
    }

    private fun handleLoadingState() = with(binding) {
        refreshLayout.isRefreshing = true
    }

    private fun handleSuccessState(taskListState: TaskListState.Success) = with(binding) {
        refreshLayout.isEnabled = taskListState.toDoList.isNotEmpty()
        refreshLayout.isRefreshing = false

        if (taskListState.toDoList.isEmpty()) {
            emptyText.isVisible = true
            taskList.isGone = true
        } else {
            emptyText.isGone = true
            taskList.isVisible = true
            adapter.setToDoList(
                taskListState.toDoList,
                toDoItemClickListener = {
                    startActivityForResult(
                        DetailActivity.getIntent(this@ListActivity, it.id, DetailMode.DETAIL),
                        DetailActivity.FETCH_REQUEST_CODE
                    )
                }, toDoCheckListener = {
                    viewModel.updateEntity(it)
                }
            )
        }
    }

    private fun handleErrorState() {
        Toast.makeText(this, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_all -> {
                viewModel.deleteAll()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}