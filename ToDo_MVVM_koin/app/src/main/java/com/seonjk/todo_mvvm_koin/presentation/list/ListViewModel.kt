package com.seonjk.todo_mvvm_koin.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.seonjk.todo_mvvm_koin.data.entity.ToDoEntity
import com.seonjk.todo_mvvm_koin.domain.usecase.DeleteAllToDoItemUseCase
import com.seonjk.todo_mvvm_koin.domain.usecase.GetToDoListUseCase
import com.seonjk.todo_mvvm_koin.domain.usecase.UpdateToDoItemUseCase
import com.seonjk.todo_mvvm_koin.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class ListViewModel(
    private val getToDoListUseCase: GetToDoListUseCase,
    private val updateToDoItemUseCase: UpdateToDoItemUseCase,
    private val deleteAllToDoItemUseCase: DeleteAllToDoItemUseCase
) : BaseViewModel() {

    private var _taskListLiveData = MutableLiveData<TaskListState>(TaskListState.Uninitialized)
    val taskListLiveData: LiveData<TaskListState> = _taskListLiveData
    override fun fetchData(): Job = viewModelScope.launch {
        _taskListLiveData.postValue(TaskListState.Loading)
        _taskListLiveData.postValue(TaskListState.Success(getToDoListUseCase()))
    }

    fun updateEntity(toDoEntity: ToDoEntity) = viewModelScope.launch {
        updateToDoItemUseCase(toDoEntity)
    }

    fun deleteAll() = viewModelScope.launch{
        _taskListLiveData.postValue(TaskListState.Loading)
        deleteAllToDoItemUseCase()
        _taskListLiveData.postValue(TaskListState.Success(getToDoListUseCase()))
    }
}