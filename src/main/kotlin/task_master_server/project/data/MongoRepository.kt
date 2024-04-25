package task_master_server.project.data

import task_master_server.project.model.Tasks
import task_master_server.project.model.UserDetails
import ui.domain.TaskDto

interface MongoRepository {

//    fun checkUserExistence(user: User): Boolean?

    suspend fun addUser(user: UserDetails): String?

    suspend fun signInExistingUser(_id: String, updatedToken: String)

//    fun getUser(userId: Id<UserDetails>): UserDetails

    suspend fun addTask(task: Tasks, userId: String): String?

    suspend fun getAllTask(userId: String): List<TaskDto>?

    suspend fun getTask(taskId: String, userId: String): TaskDto?

    suspend fun deleteTask(taskId: String, userId: String)

    suspend fun updateTask(taskDto: TaskDto, userId: String): String?

}