package com.seonjk.todo_mvvm.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job

@HiltViewModel
internal abstract class BaseViewModel : ViewModel() {

    abstract fun fetchData(): Job
}