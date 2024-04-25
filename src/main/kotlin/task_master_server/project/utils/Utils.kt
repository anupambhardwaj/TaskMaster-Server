package task_master_server.project.utils

import task_master_server.project.model.ApiResponse
import task_master_server.project.model.Tasks
import ui.domain.TaskDto

const val SUCCESS = "Success"
const val FAIL = "Fail"

fun List<Tasks>?.convertToTaskDtoList(): List<TaskDto> {
    val taskDtoList = mutableListOf<TaskDto>()
    if (this == null)
        return emptyList()
    this.forEach {
        val taskDtoObject = TaskDto(_id = it._id, title = it.title, description = it.description, completed = it.completed, pinned = it.pinned)
        taskDtoList.add(taskDtoObject)
    }
    return taskDtoList
}

fun TaskDto.convertToTask(userId: String): Tasks {
    return Tasks(this._id, this.title, this.description, this.completed, this.pinned, userId)
}

fun Tasks?.convertToTask(): TaskDto? {
    if (this == null)
        return null

    return TaskDto(this._id, this.title, this.description, this.completed, this.pinned)
}