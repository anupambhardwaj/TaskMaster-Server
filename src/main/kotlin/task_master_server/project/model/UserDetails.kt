package task_master_server.project.model

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

data class UserDetails(
    @BsonId
    val _id: String? = null,
    val email: String,
    val password: String,
    val token: String,
)
