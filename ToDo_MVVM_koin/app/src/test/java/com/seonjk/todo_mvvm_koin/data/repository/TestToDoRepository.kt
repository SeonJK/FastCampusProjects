package com.seonjk.todo_mvvm_koin.data.repository

import com.seonjk.todo_mvvm.data.repository.ToDoRepository
import com.seonjk.todo_mvvm_koin.data.entity.ToDoEntity

class TestToDoRepository : ToDoRepository {

    private val toDoList: MutableList<ToDoEntity> = mutableListOf()
    override suspend fun getToDoList(): List<ToDoEntity> {
        return toDoList
    }

    override suspend fun getToDoItem(id: Long): ToDoEntity? {
        return toDoList.find { it.id == id }
    }

    override suspend fun insertToDoItem(toDoEntity: ToDoEntity): Long {
        TODO("Not yet implemented")
    }

    override suspend fun insertToDoList(toDoList: List<ToDoEntity>) {
        this.toDoList.addAll(toDoList)
    }

    override suspend fun updateToDoItem(toDoEntity: ToDoEntity) {
        val foundToDoEntity = toDoList.find { it.id == toDoEntity.id }
        this.toDoList[toDoList.indexOf(foundToDoEntity)] = toDoEntity
    }

    override suspend fun deleteToDoItem(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        toDoList.clear()
    }
}