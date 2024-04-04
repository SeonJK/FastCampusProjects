package com.seonjk.todo_mvvm_koin.domain.usecase

import com.seonjk.todo_mvvm.data.repository.ToDoRepository
import com.seonjk.todo_mvvm_koin.data.entity.ToDoEntity
import com.seonjk.todo_mvvm_koin.domain.UseCase

class GetToDoItemUseCase(
    private val repository: ToDoRepository
) : UseCase {

    suspend operator fun invoke(id: Long): ToDoEntity? = repository.getToDoItem(id)
}