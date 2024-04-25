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

fun Route.authenticateRoute(mongoDbService: MongoDbService) {
    authenticate(FIREBASE_AUTH) {
        get("/authenticated") {
            try {
                val user: User =
                    call.principal() ?: return@get call.respond(
                        message = ApiResponse(
                            success = false,
                            message = ErrorResponse.UNAUTHORIZED_REQUEST_RESPONSE.message,
                            data = null
                        ), status = HttpStatusCode.Unauthorized
                    )
                call.respond(HttpStatusCode.OK, "My name is ${user._id}, and I'm authenticated!")
            } catch (ex: Exception) {
                return@get call.respond(
                    message = ApiResponse(
                        success = false,
                        message = ex.message,
                        data = null
                    ), status = HttpStatusCode.Unauthorized
                )
            }

        }
    }
}