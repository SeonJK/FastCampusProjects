package com.seonjk.todo_mvvm_koin.data.repository.impl

import com.seonjk.todo_mvvm.data.repository.ToDoRepository
import com.seonjk.todo_mvvm_koin.data.entity.ToDoEntity
import com.seonjk.todo_mvvm_koin.data.local_db.dao.ToDoDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ToDoRepositoryImpl(
    private val toDoDao: ToDoDao,
    private val ioDispatcher: CoroutineDispatcher
) : ToDoRepository {
    override suspend fun getToDoList(): List<ToDoEntity> = withContext(ioDispatcher) {
        toDoDao.getAll()
    }
    override suspend fun getToDoItem(id: Long): ToDoEntity? = withContext(ioDispatcher) {
        toDoDao.getItem(id)
    }

    override suspend fun insertToDoItem(toDoEntity: ToDoEntity): Long = withContext(ioDispatcher) {
        toDoDao.insert(toDoEntity)
    }

    override suspend fun insertToDoList(toDoList: List<ToDoEntity>) = withContext(ioDispatcher) {
        toDoDao.insert(toDoList)
    }

    override suspend fun updateToDoItem(toDoEntity: ToDoEntity) = withContext(ioDispatcher) {
        toDoDao.update(toDoEntity)
    }

    override suspend fun deleteToDoItem(id: Long) = toDoDao.deleteItem(id)

    override suspend fun deleteAll() = toDoDao.deleteAll()
}