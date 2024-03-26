package com.seonjk.todo_mvvm.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seonjk.todo_mvvm.data.entity.ToDoEntity
import com.seonjk.todo_mvvm.databinding.ItemTaskBinding
import javax.inject.Inject

class TaskListAdapter constructor(
    private val toDoEntity: ToDoEntity
) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    private var toDoList: List<ToDoEntity> = listOf()
    private lateinit var toDoItemClickListener: (ToDoEntity) -> Unit
    private lateinit var toDoCheckListener: (ToDoEntity) -> Unit

    inner class TaskViewHolder(binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(toDoEntity: ToDoEntity) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int = toDoList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(toDoList[position])
    }
}