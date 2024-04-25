package task_master_server.project.model

import org.bson.codecs.pojo.annotations.BsonId

data class UserTaskReference(
    @BsonId
    val _id: String? = null,
    val taskIdList: List<String>
) {


}