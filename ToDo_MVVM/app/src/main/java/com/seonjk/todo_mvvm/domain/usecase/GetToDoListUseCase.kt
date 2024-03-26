package com.seonjk.todo_mvvm.domain.usecase

import com.seonjk.todo_mvvm.data.entity.ToDoEntity
import com.seonjk.todo_mvvm.data.repository.ToDoRepository
import com.seonjk.todo_mvvm.domain.UseCase

internal class GetToDoListUseCase(
    private val toDoRepository: ToDoRepository
) : UseCase {

    suspend operator fun invoke(): List<ToDoEntity> = toDoRepository.getToDoList()
}