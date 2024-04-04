package com.seonjk.todo_mvvm_koin.di

import com.seonjk.todo_mvvm.data.repository.ToDoRepository
import com.seonjk.todo_mvvm_koin.data.repository.TestToDoRepository
import com.seonjk.todo_mvvm_koin.domain.usecase.DeleteAllToDoItemUseCase
import com.seonjk.todo_mvvm_koin.domain.usecase.GetToDoItemUseCase
import com.seonjk.todo_mvvm_koin.domain.usecase.GetToDoListUseCase
import com.seonjk.todo_mvvm_koin.domain.usecase.InsertToDoLIstUseCase
import com.seonjk.todo_mvvm_koin.domain.usecase.UpdateToDoItemUseCase
import com.seonjk.todo_mvvm_koin.presentation.list.ListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appTestModule = module {

    // ViewModel
    viewModel { ListViewModel(get(), get(), get()) }

    // UseCase
    factory { GetToDoListUseCase(get()) }
    factory { InsertToDoLIstUseCase(get()) }
    factory { UpdateToDoItemUseCase(get()) }
    factory { GetToDoItemUseCase(get()) }
    factory { DeleteAllToDoItemUseCase(get()) }

    // Repository
    single<ToDoRepository> { TestToDoRepository() }
}