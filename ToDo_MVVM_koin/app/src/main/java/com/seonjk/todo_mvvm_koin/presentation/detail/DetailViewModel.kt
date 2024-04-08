package com.seonjk.todo_mvvm_koin.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.seonjk.todo_mvvm_koin.data.entity.ToDoEntity
import com.seonjk.todo_mvvm_koin.domain.usecase.DeleteToDoItemUseCase
import com.seonjk.todo_mvvm_koin.domain.usecase.GetToDoItemUseCase
import com.seonjk.todo_mvvm_koin.domain.usecase.InsertToDoItemUseCase
import com.seonjk.todo_mvvm_koin.domain.usecase.UpdateToDoItemUseCase
import com.seonjk.todo_mvvm_koin.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class DetailViewModel(
    var detailMode: DetailMode,
    var id: Long = -1,
    private val getToDoItemUseCase: GetToDoItemUseCase,
    private val deleteToDoItemUseCase: DeleteToDoItemUseCase,
    private val updateToDoItemUseCase: UpdateToDoItemUseCase,
    private val insertToDoItemUseCase: InsertToDoItemUseCase
) : BaseViewModel() {

    private var  _taskDetailLiveData = MutableLiveData<TaskDetailState>(TaskDetailState.Uninitialized)
    val taskDetailLiveData: LiveData<TaskDetailState> = _taskDetailLiveData
    override fun fetchData(): Job = viewModelScope.launch{
        when(detailMode) {
            DetailMode.DETAIL -> {
                _taskDetailLiveData.postValue(TaskDetailState.Loading)
                try {
                    getToDoItemUseCase(id)?.let { todoEntity ->
                        _taskDetailLiveData.postValue(TaskDetailState.Success(todoEntity))
                    } ?: kotlin.run {
                        _taskDetailLiveData.postValue(TaskDetailState.Error)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _taskDetailLiveData.postValue(TaskDetailState.Error)
                }
            }
            DetailMode.WRITE -> {
                _taskDetailLiveData.postValue(TaskDetailState.Write)
            }
        }
    }

    fun deleteTask() = viewModelScope.launch{
        _taskDetailLiveData.postValue(TaskDetailState.Loading)
        try {
            deleteToDoItemUseCase(id)
            _taskDetailLiveData.postValue(TaskDetailState.Delete)
        } catch (e: Exception) {
            e.printStackTrace()
            _taskDetailLiveData.postValue(TaskDetailState.Error)
        }
    }

    fun setModifyMode() = viewModelScope.launch {
        _taskDetailLiveData.postValue(TaskDetailState.Modify)
    }

    fun writeTask(title: String, description: String) = viewModelScope.launch {
        _taskDetailLiveData.postValue(TaskDetailState.Loading)
        when(detailMode) {
            DetailMode.DETAIL -> {
                try {
                    getToDoItemUseCase(id)?.let {
                        val updateTodoEntity = it.copy(
                            title = title,
                            description = description
                        )
                        updateToDoItemUseCase(updateTodoEntity)
                        _taskDetailLiveData.postValue(TaskDetailState.Success(updateTodoEntity))
                    } ?: kotlin.run {
                        _taskDetailLiveData.postValue(TaskDetailState.Error)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _taskDetailLiveData.postValue(TaskDetailState.Error)
                }
            }
            DetailMode.WRITE -> {
                try {
                    val writeToDoEntity = ToDoEntity(
                        title = title,
                        description = description,
                        hasCompleted = false
                    )
                    _taskDetailLiveData.postValue(TaskDetailState.Loading)
                    id = insertToDoItemUseCase(writeToDoEntity)
                    _taskDetailLiveData.postValue(TaskDetailState.Success(writeToDoEntity))
                    detailMode = DetailMode.DETAIL
                } catch (e: Exception) {
                    e.printStackTrace()
                    _taskDetailLiveData.postValue(TaskDetailState.Error)
                }
            }
        }
    }
}