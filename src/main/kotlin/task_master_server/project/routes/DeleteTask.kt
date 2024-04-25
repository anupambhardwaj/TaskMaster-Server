package task_master_server.project.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import task_master_server.project.auth.firebase.FIREBASE_AUTH
import task_master_server.project.data.MongoDbService
import task_master_server.project.model.ApiResponse
import task_master_server.project.model.ErrorResponse
import task_master_server.project.model.User
import task_master_server.project.utils.SUCCESS

fun Route.deleteTask(mongoDbService: MongoDbService) {
    authenticate(FIREBASE_AUTH) {
        get("/deleteTask") {

            val user: User = call.principal() ?: return@get call.respond(
                message = ApiResponse(
                    success = false,
                    message = ErrorResponse.UNAUTHORIZED_REQUEST_RESPONSE.message,
                    data = null
                ), status = HttpStatusCode.Unauthorized
            )

            try {

                val taskId = call.request.queryParameters["taskId"]
                if (taskId != "")
                    throw java.lang.NumberFormatException()

                mongoDbService.deleteTask(taskId, user._id)

                return@get call.respond(
                    message = ApiResponse(
                        success = true,
                        message = SUCCESS,
                        data = null
                    ), status = HttpStatusCode.OK
                )

            } catch (ex: NumberFormatException) {
                return@get call.respond(
                    message = ApiResponse(
                        success = false,
                        message = ErrorResponse.TASK_NOT_FOUND_RESPONSE.message,
                        data = null
                    ), status = HttpStatusCode.OK
                )
            } catch (ex: IllegalArgumentException) {
                return@get call.respond(
                    message = ApiResponse(
                        success = false,
                        message = ErrorResponse.TASK_NOT_FOUND_RESPONSE.message,
                        data = null
                    ), status = HttpStatusCode.OK
                )
            }

        }
    }

}