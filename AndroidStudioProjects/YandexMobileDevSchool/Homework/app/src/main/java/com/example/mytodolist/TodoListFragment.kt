package com.example.mytodolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TodoListFragment : Fragment() {

    private lateinit var rv: RecyclerView
    private lateinit var adapter: TodoListAdapter
    private lateinit var visibilityButton: ImageView

    private var isFiltered: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_todo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv = view.findViewById(R.id.recycler)
        visibilityButton = view.findViewById(R.id.visibilityButton)

        updateVisibilityButtonIcon()

        val data = TodoItemsRepository()
        adapter = TodoListAdapter(data.getAllTodoItems(), requireActivity() as MainActivity)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(requireContext())

        visibilityButton.setOnClickListener {
            isFiltered = !isFiltered
            updateVisibilityButtonIcon()
        }

        val fab: FloatingActionButton = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            (activity as MainActivity).replaceToAddTaskFragment()
        }
    }

    private fun updateVisibilityButtonIcon() {
        val icon = if (isFiltered) {
            R.drawable.visibility_off
        } else {
            R.drawable.visibility
        }
        visibilityButton.setImageResource(icon)
    }
}
