package task_master_server.project.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse <T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null
)
