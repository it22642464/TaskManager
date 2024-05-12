package com.example.taskmanager

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import java.util.Date

//Database class using Task entities
@Database(entities = [Task::class], version = 1)
abstract class Database : RoomDatabase() {
	abstract fun taskDao(): TaskDao
}

//Dao interface for interacting with database
@Dao
interface TaskDao {
	//Define SQL functions to use by DAO object
	@Query("SELECT * FROM task")
	suspend fun getAll(): MutableList<Task>
	@Query("SELECT * FROM task WHERE taskName = :taskName")
	suspend fun getTaskByName(taskName: String): Task?
	@Insert
	suspend fun insertAll(vararg tasks: Task)
	@Update(entity = Task::class)
	suspend fun updateTask(task : Task)
	@Delete
	suspend fun delete(task: Task)

	suspend fun toggleCompleted(taskName: String) {
		val task = getTaskByName(taskName)
		task?.let {
			val updatedTask = it.copy(taskCompleted = !it.taskCompleted)
			updateTask(updatedTask)
		}
	}
}

//Table definition for Task Entities
@Entity
data class Task(
	@PrimaryKey val taskName: String,
	@ColumnInfo(name = "task_completed") var taskCompleted: Boolean = false,
)