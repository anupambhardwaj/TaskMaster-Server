package task_master_server.project.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import task_master_server.project.auth.firebase.FIREBASE_AUTH
import task_master_server.project.data.MongoDbService
import task_master_server.project.model.ApiResponse
import task_master_server.project.model.ErrorResponse
import task_master_server.project.model.User
import task_master_server.project.utils.convertToTask
import ui.domain.TaskDto

fun Route.addTask(mongoDbService: MongoDbService) {
    authenticate(FIREBASE_AUTH) {
        post("/addTask") {
        val user: User = call.principal() ?:
        return@post call.respond(
            message = ApiResponse(
                success = false,
                message = ErrorResponse.UNAUTHORIZED_REQUEST_RESPONSE.message,
                data = null
            ), status = HttpStatusCode.Unauthorized
        )

        val task = call.receive<TaskDto>()
        val id = mongoDbService.addTask(task.convertToTask(user._id), userId = user._id)
            id?.let {
                return@post call.respond(
                    message = ApiResponse(
                        success = true,
                        message = "Task added Successfully",
                        data = "id: $it"
                    ), status = HttpStatusCode.OK
                )
            } ?: return@post call.respond(
            message = ApiResponse(
                success = false,
                message = ErrorResponse.BAD_REQUEST_RESPONSE.message,
                data = null
            ), status = HttpStatusCode.BadRequest
        )
    }
    }
}