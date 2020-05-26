package com.alfarabi.todo_list.todo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alfarabi.todo_list.R
import kotlinx.android.synthetic.main.item_todolist.view.*

class TodoListAdapter(private val context: Context?, private val listener: (TodoList, Int) -> Unit):
        RecyclerView.Adapter<TodoListViewHolder>(){

    private var todolists = listOf<TodoList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        return TodoListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_todolist,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = todolists.size

    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
        if (context != null) {
            holder.bindItem(context, todolists[position], listener)
        }
    }

    fun setTodolists(todolists: List<TodoList>){
        this.todolists = todolists
        notifyDataSetChanged()
    }
}

class TodoListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun bindItem(context: Context?, todoList: TodoList, listener: (TodoList, Int) -> Unit){
        itemView.todolist.text = todoList.note

        itemView.setOnClickListener{
            listener(todoList, layoutPosition)
        }
    }
}