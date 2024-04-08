package com.seonjk.todo_mvvm_koin.presentation.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.seonjk.todo_mvvm_koin.databinding.ActivityDetailBinding
import com.seonjk.todo_mvvm_koin.presentation.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

internal class DetailActivity : BaseActivity<DetailViewModel>() {

    private lateinit var binding: ActivityDetailBinding

    override val viewModel: DetailViewModel by viewModel {
        parametersOf(
            intent.getSerializableExtra(DETAIL_MODE_KEY),
            intent.getLongExtra(TODO_ID_KEY, -1)
        )
    }

    companion object {
        const val TODO_ID_KEY = "ToDoId"
        const val DETAIL_MODE_KEY = "DetailMode"

        const val FETCH_REQUEST_CODE = 10

        fun getIntent(context: Context, detailMode: DetailMode) =
            Intent(context, DetailActivity::class.java).apply {
                putExtra(DETAIL_MODE_KEY, detailMode)
            }

        fun getIntent(context: Context, id: Long, detailMode: DetailMode) =
            Intent(context, DetailActivity::class.java).apply {
                putExtra(TODO_ID_KEY, id)
                putExtra(DETAIL_MODE_KEY, detailMode)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setResult(Activity.RESULT_OK)
    }

    override fun observeData() = viewModel.taskDetailLiveData.observe(this) { state ->
        when (state) {
            is TaskDetailState.Uninitialized -> initViews()
            is TaskDetailState.Loading -> handleLoadingState()
            is TaskDetailState.Success -> handleSuccessState(state)
            is TaskDetailState.Modify -> handleModifyState()
            is TaskDetailState.Delete -> handleDeleteState()
            is TaskDetailState.Error -> handleErrorState()
            is TaskDetailState.Write -> handleWriteState()
        }
    }

    private fun initViews() = with(binding) {
        titleInput.isEnabled = false
        descriptionInput.isEnabled = false

        deleteButton.isGone = true
        modifyButton.isGone = true
        updateButton.isGone = true

        deleteButton.setOnClickListener {
            viewModel.deleteTask()
        }
        modifyButton.setOnClickListener {
            viewModel.setModifyMode()
        }
        updateButton.setOnClickListener {
            viewModel.writeTask(
                title = titleInput.text.toString(),
                description = descriptionInput.text.toString()
            )
        }
    }

    private fun handleLoadingState() = with(binding) {
        progressBar.isVisible = true
    }

    private fun handleSuccessState(state: TaskDetailState.Success) = with(binding) {
        progressBar.isGone = true

        titleInput.isEnabled = false
        descriptionInput.isEnabled = false

        deleteButton.isVisible = true
        modifyButton.isVisible = true
        updateButton.isGone = true

        val toDoItem = state.toDoItem
        titleInput.setText(toDoItem.title)
        descriptionInput.setText(toDoItem.description)
    }

    private fun handleModifyState() = with(binding) {
        titleInput.isEnabled = true
        descriptionInput.isEnabled = true

        deleteButton.isGone = true
        modifyButton.isGone = true
        updateButton.isVisible = true
    }

    private fun handleDeleteState() {
        Toast.makeText(this, "성공적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun handleErrorState() {
        Toast.makeText(this, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun handleWriteState() = with(binding) {
        titleInput.isEnabled = true
        descriptionInput.isEnabled = true

        updateButton.isVisible = true
    }
}