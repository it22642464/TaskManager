package com.example.taskmanager

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.runBlocking

@Composable
fun TaskItem(task: Task, db : Database) {
	var isChecked by remember {
		mutableStateOf(task.taskCompleted)
	}
	var taskDao = db.taskDao()
	Row(
		modifier = Modifier.padding(10.dp)
	) {
		Text(
			text = task.taskName,
			modifier = Modifier.weight(1f),
			textDecoration = if (isChecked) TextDecoration.LineThrough else null
		)
		Button(
			onClick = {
				isChecked = !isChecked
				task.taskCompleted = !task.taskCompleted
				runBlocking {
					taskDao.updateTask(task)
				}
			}
		) {
			if (isChecked) {
				Icon(Icons.Default.Refresh, contentDescription = null)
			}
			else {
				Icon(Icons.Default.Check, contentDescription = null)
			}
		}
	}
}