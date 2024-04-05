package com.seonjk.todo_mvvm_koin.di

import android.content.Context
import androidx.room.Room
import com.seonjk.todo_mvvm.data.repository.ToDoRepository
import com.seonjk.todo_mvvm_koin.data.local_db.ToDoDatabase
import com.seonjk.todo_mvvm_koin.data.repository.impl.ToDoRepositoryImpl
import com.seonjk.todo_mvvm_koin.domain.usecase.DeleteAllToDoItemUseCase
import com.seonjk.todo_mvvm_koin.domain.usecase.DeleteToDoItemUseCase
import com.seonjk.todo_mvvm_koin.domain.usecase.GetToDoItemUseCase
import com.seonjk.todo_mvvm_koin.domain.usecase.GetToDoListUseCase
import com.seonjk.todo_mvvm_koin.domain.usecase.InsertToDoItemUseCase
import com.seonjk.todo_mvvm_koin.domain.usecase.InsertToDoLIstUseCase
import com.seonjk.todo_mvvm_koin.domain.usecase.UpdateToDoItemUseCase
import com.seonjk.todo_mvvm_koin.presentation.detail.DetailMode
import com.seonjk.todo_mvvm_koin.presentation.detail.DetailViewModel
import com.seonjk.todo_mvvm_koin.presentation.list.ListViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appModule = module {

    single { Dispatchers.Main }
    single { Dispatchers.IO }

    // ViewModel
    viewModel { ListViewModel(get(), get(), get()) }
    viewModel { (detailMode: DetailMode, id: Long) ->
        DetailViewModel(
            detailMode = detailMode,
            id = id,
            get(),
            get(),
            get(),
            get()
        )
    }

    // UseCase
    factory { GetToDoListUseCase(get()) }
    factory { GetToDoItemUseCase(get()) }
    factory { InsertToDoLIstUseCase(get()) }
    factory { InsertToDoItemUseCase(get()) }
    factory { UpdateToDoItemUseCase(get()) }
    factory { DeleteAllToDoItemUseCase(get()) }
    factory { DeleteToDoItemUseCase(get()) }

    // Repository
    single<ToDoRepository> { ToDoRepositoryImpl(get(), get()) }

    // Database
    single { provideDB(androidApplication()) }
    single { provideToDoDao(get()) }
}

internal fun provideDB(context: Context): ToDoDatabase =
    Room.databaseBuilder(context, ToDoDatabase::class.java, ToDoDatabase.DB_NAME).build()

internal fun provideToDoDao(database: ToDoDatabase) = database.toDoDao()