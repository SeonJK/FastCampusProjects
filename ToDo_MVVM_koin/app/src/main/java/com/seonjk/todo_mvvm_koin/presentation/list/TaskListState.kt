package com.seonjk.todo_mvvm_koin.presentation.list

sealed class TaskListState {
    object Uninitialized: TaskListState()

    object Loading: TaskListState()

    data class Success(
        val toDoList: List<Any>
    ): TaskListState()

    object Error: TaskListState()
}