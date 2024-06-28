package com.example.mytodolist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


//Во избежание недопонятости оставляю комментарий. По тз не надо было делать
//удаление, добавление и фильтрацию по выполненности(написано, что это доп балл).
//Как я понял нужно было реализовать просто UI с минимальной логикой перехода.
//
//В остальном прошу если есть какие-то комментарии по кодстайлу и чистоте кода
//расписать их, т.к кодстайл котлина мне плохо известен и я буду рад если мы поможем друг другу :)
//
//Еще прошу простить, вероятно, за не самый адекватный и аккуратный код, у меня было 4 экзамена на неделе
//я писал это с утра пятницы до 5 утра субботы :(



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            replaceFragment(TodoListFragment())
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragmentManager = supportFragmentManager
                if (fragmentManager.backStackEntryCount > 1) {
                    fragmentManager.popBackStack()
                } else if (fragmentManager.backStackEntryCount == 1) {
                    finish()
                }
            }
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
    fun replaceToAddTaskFragment() {
        replaceFragment(AddTaskFragment())
    }
}

class TodoListAdapter(
    private val todoItems: List<TodoItem>,
    private val activity: MainActivity
) : RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        private val todoText: TextView = itemView.findViewById(R.id.todoText)
        private val infoIcon: ImageView = itemView.findViewById(R.id.infoIcon)
        private val main: LinearLayout = itemView.findViewById(R.id.main)

        fun bind(todoItem: TodoItem) {
            val priorityIcon: ImageView = itemView.findViewById(R.id.priorityIcon)
            if (todoItem.priority != Priority.NORMAL) {
                priorityIcon.visibility = View.VISIBLE
                priorityIcon.setImageResource(if (todoItem.priority == Priority.HIGH) R.drawable.high_priority else R.drawable.low_priority)
            } else {
                priorityIcon.visibility = View.GONE
            }
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                todoItem.isCompleted = isChecked
            }
            todoText.text = todoItem.text
            Log.d("ASD", todoText.text as String)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todoItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todoItem = todoItems[position]
        holder.bind(todoItem)
        holder.itemView.setOnClickListener {
            (activity as MainActivity).replaceToAddTaskFragment()
        }
    }

}