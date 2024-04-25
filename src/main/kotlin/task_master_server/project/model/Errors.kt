package task_master_server.project.model

import kotlinx.serialization.Serializable

@Serializable
data class Errors(
    val message: String,
    val domain: String,
    val reason: String
)
