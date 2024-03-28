package com.seonjk.todo_mvvm_koin.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.seonjk.todo_mvvm_koin.domain.usecase.GetToDoListUseCase
import com.seonjk.todo_mvvm_koin.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class ListViewModel(
    private val getDoListUseCase: GetToDoListUseCase
) : BaseViewModel() {

    private var _taskList = MutableLiveData<TaskListState>(TaskListState.UnInitialized)
    val taskList: LiveData<TaskListState> = _taskList
    override fun fetchData(): Job = viewModelScope.launch {
        // TODO: 로컬 DB에서 데이터를 불러오는 유즈케이스
        _taskList.postValue(TaskListState.Loading)
        _taskList.postValue(TaskListState.Success(getDoListUseCase()))
    }
}