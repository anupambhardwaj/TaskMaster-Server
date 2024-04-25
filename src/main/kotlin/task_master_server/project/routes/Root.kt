package task_master_server.project.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.root() {
    get("/") {
        call.respond(HttpStatusCode.OK, "I'm working just fine, thanks!")
    }
}