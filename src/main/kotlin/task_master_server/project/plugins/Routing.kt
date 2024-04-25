package task_master_server.project.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import task_master_server.project.data.MongoDbService
import task_master_server.project.routes.*

fun Application.configureRouting(mongoDbService: MongoDbService) {
    routing {

        root()
        authenticateRoute(mongoDbService)
        signUp(mongoDbService)
        signIn(mongoDbService)
        getTask(mongoDbService) //TODO Handle Functionality
        getAllTask(mongoDbService) //TODO Handle Functionality
        addTask(mongoDbService)
        updateTask(mongoDbService) //TODO Handle Functionality
        deleteTask(mongoDbService) //TODO Handle Functionality

    }
}



