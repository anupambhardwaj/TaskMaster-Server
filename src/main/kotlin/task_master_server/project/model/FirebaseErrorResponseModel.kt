package task_master_server.project.model

import kotlinx.serialization.Serializable

@Serializable
data class FirebaseErrorResponseModel(
    val code: Int,
    val message: String,
    val errors: List<Errors>
)
