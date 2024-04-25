package task_master_server.project.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import task_master_server.project.auth.firebase.firebase
import task_master_server.project.model.User


fun Application.configureAuthentication() {
    install(Authentication) {
        firebase {
            validate {
                println("uid = ${it.uid}, name = ${it.name}")
                User(it.uid, it.name.orEmpty())

            }
        }
    }
}
