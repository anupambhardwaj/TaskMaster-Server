package task_master_server.project

import io.ktor.server.application.*
import task_master_server.project.config.firebase.FirebaseAdmin
import task_master_server.project.data.MongoDbService
import task_master_server.project.plugins.configureAuthentication
import task_master_server.project.plugins.configureMonitoring
import task_master_server.project.plugins.configureRouting
import task_master_server.project.plugins.configureSerialization

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val mongoDbService = MongoDbService()

    FirebaseAdmin.init()

    configureAuthentication()
    configureSerialization()
    configureMonitoring()
    configureRouting(mongoDbService)
}
