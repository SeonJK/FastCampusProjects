package com.seonjk.todo_mvvm_koin.domain.usecase

import com.seonjk.todo_mvvm.data.repository.ToDoRepository
import com.seonjk.todo_mvvm_koin.data.entity.ToDoEntity
import com.seonjk.todo_mvvm_koin.domain.UseCase

internal class InsertToDoLIstUseCase(
    private val repository: ToDoRepository
) : UseCase {

    suspend operator fun invoke(list: List<ToDoEntity>) = repository.insertToDoList(list)

}