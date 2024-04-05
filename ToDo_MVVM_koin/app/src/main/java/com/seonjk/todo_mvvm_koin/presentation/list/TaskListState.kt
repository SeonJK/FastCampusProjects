package com.seonjk.todo_mvvm_koin.presentation.list

import com.seonjk.todo_mvvm_koin.data.entity.ToDoEntity

sealed class TaskListState {
    object Uninitialized: TaskListState()

    object Loading: TaskListState()

    data class Success(
        val toDoList: List<ToDoEntity>
    ): TaskListState()

    object Error: TaskListState()
}