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
 * 1. test viewModel fetch
 * 2. test Task Insert
 *
 * */
internal class DetailViewModelForWriteTest : ViewModelTest() {

    private val id = 1L

    private val viewModel: DetailViewModel by inject { parametersOf(DetailMode.WRITE, id ) }
    private val listViewModel: ListViewModel by inject()

    private val insertToDoItemUseCase: InsertToDoItemUseCase by inject()

    private val todo = ToDoEntity(
        id = id,
        title = "title $id",
        description = "description $id",
        hasCompleted = false
    )

    // Test : 입력된 데이터를 불러와서 검증
    @Test
    fun `test viewModel fetch`(): Unit = runTest {
        val testObservable = viewModel.taskDetailLiveData.test()
        viewModel.fetchData()
        testObservable.assertValueSequence(
            listOf(
                TaskDetailState.Uninitialized,
                TaskDetailState.Write
            )
        )
    }

    @Test
    fun `test Task Insert`(): Unit = runTest {
        val detailTestObservable = viewModel.taskDetailLiveData.test()
        val listTestObservable = listViewModel.taskListLiveData.test()

        viewModel.writeTask(
            title = todo.title,
            description = todo.description
        )

        detailTestObservable.assertValueSequence(
            listOf(
                TaskDetailState.Uninitialized,
                TaskDetailState.Loading,
                TaskDetailState.Success(todo)
            )
        )

        assert(viewModel.detailMode == DetailMode.DETAIL)
        assert(viewModel.id == id)

        // 뒤로나가서 리스트 보기
        listViewModel.fetchData()
        listTestObservable.assertValueSequence(
            listOf(
                TaskListState.Uninitialized,
                TaskListState.Loading,
                TaskListState.Success(listOf(todo))
            )
        )
    }
}