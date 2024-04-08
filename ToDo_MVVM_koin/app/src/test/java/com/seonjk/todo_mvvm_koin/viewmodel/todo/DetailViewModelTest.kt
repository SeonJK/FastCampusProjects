package com.seonjk.todo_mvvm_koin.viewmodel.todo

import com.seonjk.todo_mvvm_koin.data.entity.ToDoEntity
import com.seonjk.todo_mvvm_koin.domain.usecase.GetToDoItemUseCase
import com.seonjk.todo_mvvm_koin.domain.usecase.InsertToDoItemUseCase
import com.seonjk.todo_mvvm_koin.presentation.detail.DetailMode
import com.seonjk.todo_mvvm_koin.presentation.detail.DetailViewModel
import com.seonjk.todo_mvvm_koin.presentation.detail.TaskDetailState
import com.seonjk.todo_mvvm_koin.presentation.list.ListViewModel
import com.seonjk.todo_mvvm_koin.presentation.list.TaskListState
import com.seonjk.todo_mvvm_koin.viewmodel.ViewModelTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.test.inject


/**
 * [DetailViewModel]을 테스트하기 위한 Unit Test Class
 *
 * 1. initData()
 * 2. test viewModel fetch
 * 3. test Task Update
 * 4. test Task Delete
 * */
internal class DetailViewModelTest : ViewModelTest() {

    private val id = 1L

    private val viewModel: DetailViewModel by inject { parametersOf(DetailMode.DETAIL, id ) }
    private val listViewModel: ListViewModel by inject()

    private val insertToDoItemUseCase: InsertToDoItemUseCase by inject()
    private val getToDoItemUseCase: GetToDoItemUseCase by inject()

    private val todo = ToDoEntity(
        id = id,
        title = "title $id",
        description = "description $id",
        hasCompleted = false
    )

    @Before
    fun init() {
        initData()
    }

    private fun initData() = runTest {
        insertToDoItemUseCase(todo)
    }

    // Test : 입력된 데이터를 불러와서 검증
    @Test
    fun `test viewModel fetch`(): Unit = runTest {
        val testObservable = viewModel.taskDetailLiveData.test()
        viewModel.fetchData()
        testObservable.assertValueSequence(
            listOf(
                TaskDetailState.Uninitialized,
                TaskDetailState.Loading,
                TaskDetailState.Success(todo)
            )
        )
    }

    // Test :
    @Test
    fun `test Todo Delete`(): Unit = runTest {
        val detailTestObservable = viewModel.taskDetailLiveData.test()
        viewModel.deleteTask()
        detailTestObservable.assertValueSequence(
            listOf(
                TaskDetailState.Uninitialized,
                TaskDetailState.Loading,
                TaskDetailState.Delete
            )
        )

        val listTestObservable = listViewModel.taskListLiveData.test()
        listViewModel.fetchData()
        listTestObservable.assertValueSequence(
            listOf(
                TaskListState.Uninitialized,
                TaskListState.Loading,
                TaskListState.Success(listOf())
            )
        )
    }

    @Test
    fun `test Task Update`(): Unit = runTest {
        val testObservable = viewModel.taskDetailLiveData.test()

        val updateTitle = "title 1 update"
        val updateDescription = "description 1 update"

        val updateTask = todo.copy(
            title = updateTitle,
            description = updateDescription
        )

        viewModel.writeTask(
            title = updateTitle,
            description = updateDescription
        )

        testObservable.assertValueSequence(
            listOf(
                TaskDetailState.Uninitialized,
                TaskDetailState.Loading,
                TaskDetailState.Success(updateTask)
            )
        )
    }
}