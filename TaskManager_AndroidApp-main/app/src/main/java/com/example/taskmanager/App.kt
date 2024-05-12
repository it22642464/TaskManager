package com.example.taskmanager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.runBlocking

@Composable
fun App(db : Database) {
	//Initialize TaskList from DB
	val taskDao = db.taskDao()
	var tasks by remember {
		mutableStateOf(listOf<Task>())
	}
	runBlocking {
		tasks = taskDao.getAll()
	}
	Scaffold(
		floatingActionButton = {
			FloatingActionButton(onClick = {
				for (task in tasks) {
					if (task.taskCompleted) {
						tasks = tasks - task
						runBlocking {
							taskDao.delete(task)
						}
					}
				}
			}) {
				Icon(Icons.Default.Delete, contentDescription = "Delete checked")
			}
		}
	) {paddingValue ->
		Surface(
			modifier = Modifier
				.padding(paddingValue)
				.fillMaxSize()
		) {
			Column {
				var taskName by remember {
					mutableStateOf("")
				}
				Row(
					modifier = Modifier
						.fillMaxWidth()
				) {
					TextField(
						placeholder = {
							Text("Enter task...")
						},
						value = taskName,
						onValueChange = { text ->
							taskName = text
						},
						modifier = Modifier.weight(1f)
							.onKeyEvent { event ->
								when (event.key) {
									Key.Enter -> {
										if (taskName.isNotBlank()) {
											tasks = tasks + Task(taskName)
											runBlocking {
												taskDao.insertAll(Task(taskName))
											}
											taskName = ""
										}
										true
									}
									else -> false
								}
							}
					)
					Spacer(modifier = Modifier.width(10.dp)					)
					Button(
						onClick = {
						if (taskName.isNotBlank()) {
							tasks = tasks + Task(taskName)
							runBlocking {
								taskDao.insertAll(Task(taskName))
							}
							taskName = ""
						}
					}) {
						Icon(Icons.Default.Add, contentDescription = "Add Task")
					}
				}
				LazyColumn {
					items(tasks) { task ->
						TaskItem(task = task, db)
						Divider()
					}
				}
			}
		}
	}
}