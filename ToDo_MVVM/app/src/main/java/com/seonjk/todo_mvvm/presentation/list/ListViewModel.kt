package com.seonjk.todo_mvvm.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.seonjk.todo_mvvm.domain.usecase.GetToDoListUseCase
import com.seonjk.todo_mvvm.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ListViewModel @Inject constructor(
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