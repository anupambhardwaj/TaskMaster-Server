package ui.domain

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class TaskDto (
    @BsonId
    var _id: String,
    var title: String = "",
    var description: String = "",
    var completed: Boolean = false,
    var pinned: Boolean = false,
)