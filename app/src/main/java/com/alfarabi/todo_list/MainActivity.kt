package com.alfarabi.todo_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alfarabi.todo_list.todo.TodoList
import com.alfarabi.todo_list.todo.TodoListAdapter
import com.alfarabi.todo_list.todo.TodoListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var todoListViewModel: TodoListViewModel
    private lateinit var todoListAdapter: TodoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        todolist.layoutManager = LinearLayoutManager(this)
        todoListAdapter = TodoListAdapter(this){ todoList, i ->
            showAlertMenu(todoList)
        }
        todolist.adapter = todoListAdapter

        todoListViewModel = ViewModelProvider(this).get(TodoListViewModel::class.java)
        todoListViewModel.getTodolists()?.observe(this, Observer {
            todoListAdapter.setTodolists(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.addMenu -> showAlertDialogAdd()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showAlertDialogAdd(){
        val alert = AlertDialog.Builder(this)
        val editText = EditText(applicationContext)
        editText.hint = "Enter Text"
        alert.setTitle("New Note")
        alert.setView(editText)

        alert.setPositiveButton("save"){ dialog, _ ->
            todoListViewModel.insertTodoList(
                TodoList(note = editText.text.toString())
            )
            dialog.dismiss()
        }
        alert.show()
    }

    private fun showAlertMenu(todoList: TodoList){
        val items = arrayOf("Edit", "Delete")

        val builder =
            AlertDialog.Builder(this)
        builder.setItems(items){ dialog, which ->
            when (which){
                0 -> {
                    showAlertDialogEdit(todoList)
                }
                1 -> {
                    todoListViewModel.deleteTodoList(todoList)
                }
            }
        }
        builder.show()
    }

    private fun showAlertDialogEdit(todoList: TodoList){
        val alert = AlertDialog.Builder(this)
        val editText = EditText(applicationContext)
        editText.setText(todoList.note)

        alert.setTitle("Edit Note")
        alert.setView(editText)

        alert.setPositiveButton("Update") { dialog, _ ->
            todoList.note = editText.text.toString()
            todoListViewModel.updateTodoList(todoList)
            dialog.dismiss()
        }

        alert.setNegativeButton("cancel") { dialog, _ ->
            dialog.dismiss()
        }
        alert.show()
    }
}
