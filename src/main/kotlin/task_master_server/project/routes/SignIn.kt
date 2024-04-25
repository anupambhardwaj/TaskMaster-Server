package task_master_server.project.routes

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.litote.kmongo.json
import task_master_server.project.data.MongoDbService
import task_master_server.project.model.ApiResponse
import task_master_server.project.model.FirebaseRequestBody
import task_master_server.project.model.FirebaseResponseBody
import task_master_server.project.model.UserDetails

@OptIn(InternalAPI::class, ExperimentalSerializationApi::class)
fun Route.signIn(mongoDbService: MongoDbService) {
    post("/signIn") {
        val originalCall = call
        val credentials = originalCall.receive<FirebaseRequestBody>()

        try {
            val firebaseResponse = firebaseSignInRequest(mongoDbService, credentials)
            if (firebaseResponse.error == null) {
                val userDetails = UserDetails(
                    firebaseResponse.localId,
                    email = firebaseResponse.email,
                    password = credentials.password,
                    token = firebaseResponse.idToken
                )

                mongoDbService.signInExistingUser(userDetails._id!!, userDetails.token)
                val apiResponse = ApiResponse(
                    success = true,
                    message = "OK",
                    data = firebaseResponse
                )
                originalCall.respond(
                    message = apiResponse,
                    status = HttpStatusCode.OK
                )
            } else {
                val apiResponse = ApiResponse<String>(
                    success = false,
                    message = firebaseResponse.error.message,
                    data = null
                )
                originalCall.respond(message = apiResponse, status = HttpStatusCode.BadRequest)
            }

        } catch (ex: Exception) {
            val apiResponse = ApiResponse<String>(
                success = false,
                message = ex.message,
                data = null
            )
            originalCall.respond(status = HttpStatusCode.BadRequest, message = apiResponse)
        }
    }
}

private suspend fun firebaseSignInRequest(mongoDbService: MongoDbService, credentials: FirebaseRequestBody): FirebaseResponseBody {
    HttpClient(CIO) {
        install(Logging)
        install(ContentNegotiation) {
            register(
                ContentType.Text.Plain, KotlinxSerializationConverter(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                        explicitNulls = true
                    }
                )
            )
            register(
                ContentType.Application.Json, KotlinxSerializationConverter(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                        explicitNulls = true
                    }
                )
            )
        }
    }.use {
        val response: HttpResponse = it.request("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword") {
            val body = FirebaseRequestBody(
                email = credentials.email,
                password = credentials.password
            )
            method = HttpMethod.Post
            contentType(ContentType.Application.Json)
            println("body.json:  ${body.json}")
            setBody(
                body.json
            )
            parameter("key", "{YOUR_FIREBASE_KEY}")
//            url {
//                parameters.append("key", "AIzaSyAj3ZRxEEekL2EON6YVb2tXeF3P8BRHV6w")
//            }
        }
        val body = response.call.body<FirebaseResponseBody>()
        it.close()
        return body
    }
}
