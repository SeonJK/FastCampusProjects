package com.seonjk.todo_mvvm_koin.presentation.detail

import com.seonjk.todo_mvvm_koin.data.entity.ToDoEntity

sealed class TaskDetailState {

    object Uninitialized: TaskDetailState()

    object Loading: TaskDetailState()

    data class Success(
        val toDoItem: ToDoEntity
    ): TaskDetailState()

    object Delete: TaskDetailState()

    object Modify: TaskDetailState()

    object Error: TaskDetailState()

    object Write: TaskDetailState()
}