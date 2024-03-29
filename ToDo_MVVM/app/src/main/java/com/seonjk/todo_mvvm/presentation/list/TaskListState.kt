package com.seonjk.todo_mvvm.presentation.list

sealed class TaskListState {
    object UnInitialized: TaskListState()

    object Loading: TaskListState()

    data class Success(
        val toDoList: List<Any>
    ): TaskListState()

    object Error: TaskListState()
}