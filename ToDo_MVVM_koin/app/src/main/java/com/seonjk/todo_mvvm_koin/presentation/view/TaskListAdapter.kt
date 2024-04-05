package com.seonjk.todo_mvvm_koin.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.seonjk.todo_mvvm_koin.R
import com.seonjk.todo_mvvm_koin.data.entity.ToDoEntity
import com.seonjk.todo_mvvm_koin.databinding.ItemTaskBinding

class TaskListAdapter : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    private var toDoList: List<ToDoEntity> = listOf()
    private lateinit var toDoItemClickListener: (ToDoEntity) -> Unit
    private lateinit var toDoCheckListener: (ToDoEntity) -> Unit

    inner class TaskViewHolder(
        private val binding: ItemTaskBinding,
        val toDoItemClickListener: (ToDoEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(toDoEntity: ToDoEntity) = with(binding) {
            checkBox.text = toDoEntity.title
            checkToDoComplete(toDoEntity.hasCompleted)
        }

        fun bindViews(data: ToDoEntity) = with(binding) {
            checkBox.setOnClickListener {
                toDoCheckListener(
                    data.copy(hasCompleted = checkBox.isChecked)
                )
                checkToDoComplete(checkBox.isChecked)
            }
            root.setOnClickListener {
                toDoItemClickListener(data)
            }
        }

        private fun checkToDoComplete(isChecked: Boolean) = with(binding) {
            checkBox.isChecked = isChecked
            container.setBackgroundColor(
                ContextCompat.getColor(
                    root.context,
                    if (isChecked) {
                        R.color.gray_300
                    } else {
                        R.color.white
                    }
                )
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding, toDoItemClickListener)
    }

    override fun getItemCount(): Int = toDoList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bindData(toDoList[position])
        holder.bindViews(toDoList[position])
    }

    fun setToDoList(toDoList: List<ToDoEntity>, toDoItemClickListener: (ToDoEntity) -> Unit, toDoCheckListener: (ToDoEntity) -> Unit) {
        this.toDoList = toDoList
        this.toDoItemClickListener = toDoItemClickListener
        this.toDoCheckListener = toDoCheckListener
        notifyDataSetChanged()
    }
}