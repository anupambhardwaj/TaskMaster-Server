package task_master_server.project.data

import com.mongodb.ConnectionString
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.*
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import task_master_server.project.model.Tasks
import task_master_server.project.model.UserDetails
import task_master_server.project.model.UserTaskReference
import task_master_server.project.utils.convertToTask
import task_master_server.project.utils.convertToTaskDtoList
import ui.domain.TaskDto

class MongoDbService: MongoRepository {

    private var client: CoroutineClient? = null
    private var database: CoroutineDatabase? = null
    private var userCollection: CoroutineCollection<UserDetails>? = null
    private var tasksCollection: CoroutineCollection<Tasks>? = null

    init {
        if (client == null) {
            client = KMongo.createClient(ConnectionString("mongodb://localhost:27017")).coroutine
            database =  client?.getDatabase("TaskMasterDb")
            userCollection = database?.getCollection<UserDetails>("UserDetails")
            tasksCollection = database?.getCollection<Tasks>("Tasks")
        }

    }

    override suspend fun addUser(user: UserDetails): String? {
        return try {
            userCollection?.insertOne(user)
            user._id
        } catch (ex: Exception) {
            println("exception: ${ex.message}")
            null
        }

    }

    override suspend fun signInExistingUser(_id: String, updatedToken: String) {
        val user = userCollection?.findOneById(_id)
        val updatedUser = user?.copy(
            token = updatedToken
        )
        if (updatedUser != null) {
            userCollection?.updateOneById(_id, updatedUser)
        }
    }

    override suspend fun addTask(task: Tasks, userId: String): String? {
        return try {
            tasksCollection?.insertOne(task)
            task._id
        } catch (ex: Exception) {
            println("exception: ${ex.message}")
            null
        }
    }

    override suspend fun getAllTask(userId: String): List<TaskDto>? {
        return try {
            val taskDtoList = mutableListOf<TaskDto>()
            val tasksList =tasksCollection?.find(Tasks::userId eq userId)?.toList()
            tasksList.convertToTaskDtoList().also {
                taskDtoList.addAll(it)
            }
            taskDtoList
        } catch (ex: Exception) {
            null
        }
    }

    override suspend fun getTask(taskId: String, userId: String): TaskDto? {
        return try {
            var taskDto: TaskDto? = null
            tasksCollection?.findOneById(taskId)?.let {
                taskDto = it.convertToTask()
            } ?: return null
            taskDto
        } catch (ex: Exception) {
            null
        }
    }

    override suspend fun deleteTask(taskId: String, userId: String) {
        tasksCollection?.deleteOne(and(Tasks::_id eq taskId, Tasks::userId eq userId))
    }

    override suspend fun updateTask(taskDto: TaskDto, userId: String): String? {
        return try {
            val task = taskDto.convertToTask(userId)
            tasksCollection?.updateOneById(task._id, task)
            task._id
        } catch (ex: Exception) {
            println("exception: ${ex.message}")
            null
        }
    }

}