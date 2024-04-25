package task_master_server.project.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class Tasks(
    @BsonId
    var _id: String,
    var title: String = "",
    var description: String = "",
    var completed: Boolean = false,
    var pinned: Boolean = false,
    var userId: String = ""
)
