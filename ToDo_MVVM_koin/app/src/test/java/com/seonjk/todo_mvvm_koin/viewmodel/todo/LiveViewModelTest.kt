package com.seonjk.todo_mvvm_koin.viewmodel.todo

import com.seonjk.todo_mvvm_koin.data.entity.ToDoEntity
import com.seonjk.todo_mvvm_koin.domain.usecase.GetToDoItemUseCase
import com.seonjk.todo_mvvm_koin.domain.usecase.InsertToDoLIstUseCase
import com.seonjk.todo_mvvm_koin.presentation.list.ListViewModel
import com.seonjk.todo_mvvm_koin.presentation.list.TaskListState
import com.seonjk.todo_mvvm_koin.viewmodel.ViewModelTest
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

internal class LiveViewModelTest : ViewModelTest() {


    private val viewModel: ListViewModel by inject()

    private val insertToDoLIstUseCase: InsertToDoLIstUseCase by inject()
    private val getToDoItemUseCase: GetToDoItemUseCase by inject()

    private val mockList = (0 until 10).map {
        ToDoEntity(
            id = it.toLong(),
            title = "title $it",
            description = "description $it",
            hasCompleted = false
        )
    }

    @Before
    fun init() {
        initData()
    }

    private fun initData() = runTest {
        insertToDoLIstUseCase(mockList)
    }

    // Test : 입력된 데이터를 불러와서 검증
    @Test
    fun `test viewModel fetch`(): Unit = runTest {
        val testObservable = viewModel.taskList.test()
        viewModel.fetchData()
        testObservable.assertValueSequence(
            listOf(
                TaskListState.UnInitialized,
                TaskListState.Loading,
                TaskListState.Success(mockList)
            )
        )
    }

    // Test : 데이터를 업데이트 했을 때 잘 반영되는가
    @Test
    fun `test Item Update`(): Unit = runTest {
        val todo = ToDoEntity(
            id = 1,
            title = "title 1",
            description = "description 1",
            hasCompleted = true
        )
        viewModel.updateEntity(todo)
        assert((getToDoItemUseCase(todo.id)?.hasCompleted ?: false) == todo.hasCompleted)
    }

    // Test : 데이터를 다 날렸을 때 빈 상태로 보여지는가
    @Test
    fun `test Item Delete All`(): Unit = runTest {
        val testObservable = viewModel.taskList.test()
        viewModel.deleteAll()
        testObservable.assertValueSequence(
            listOf(
                TaskListState.UnInitialized,
                TaskListState.Loading,
                TaskListState.Success(listOf())
            )
        )
    }
}