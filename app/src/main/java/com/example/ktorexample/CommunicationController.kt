package com.example.androidktorexample

import android.net.Uri
import android.util.Log
import com.example.ktorexample.Menu
import com.example.ktorexample.User
import com.example.ktorexample.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object CommunicationController {
    private val BASE_URL = "https://develop.ewlab.di.unimi.it/mc/2425"
    var sid: String? = "jXdax4YQeX3ECmKh4nN5n1eJxFre2ZyZWBXsh7ocDxM6U2x3Uo3xcjKUGoLngX6t"
    var lat = 45.478
    var lng = 9.227
    private val TAG = CommunicationController::class.simpleName

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    enum class HttpMethod {
        GET,
        POST,
        DELETE,
        PUT
    }


    suspend fun genericRequest(
        url: String, method: HttpMethod,
        queryParameters: Map<String, Any> = emptyMap(),
        requestBody: Any? = null
    ): HttpResponse {

        val urlUri = Uri.parse(url)
        val urlBuilder = urlUri.buildUpon()
        queryParameters.forEach { (key, value) ->
            urlBuilder.appendQueryParameter(key, value.toString())
        }
        val completeUrlString = urlBuilder.build().toString()
        Log.d(TAG, completeUrlString)

        val request: HttpRequestBuilder.() -> Unit = {
            requestBody?.let {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
        }

        val result = when (method) {
            HttpMethod.GET -> client.get(completeUrlString, request)
            HttpMethod.POST -> client.post(completeUrlString, request)
            HttpMethod.DELETE -> client.delete(completeUrlString, request)
            HttpMethod.PUT -> client.put(completeUrlString, request)
        }
        return result
    }

    suspend fun createUser(): UserResponse {
        Log.d(TAG, "createUser")

        val url = BASE_URL + "/user"

        val httpResponse = genericRequest(url, HttpMethod.POST)
        //TODO: aggiungere il controllo degli errori
        try {
            val result: UserResponse = httpResponse.body()
            return result
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
            throw e
        }

    }
    suspend fun getUser(uid: Int): User? {
        Log.d(TAG, "getUser called for uid: $uid")

        val sid = this.sid // Recupera il SID
        if (sid == null) {
            Log.e(TAG, "SID is not set.")
            return null
        }

        val url = "$BASE_URL/user/$uid"
        val queryParams = mapOf("sid" to sid)

        return try {
            // Effettua la richiesta GET
            val httpResponse = genericRequest(url, HttpMethod.GET, queryParams)

            Log.d(TAG, "HTTP Response Status: ${httpResponse.status.value}")
            Log.d(TAG, "HTTP Response Body: ${httpResponse.bodyAsText()}")

            // Parsing della risposta
            val result: User = httpResponse.body()
            Log.d(TAG, "Parsed UserResponse: $result")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Error during getUser: ${e.message}")
            null
        }
    }

suspend fun getMenu(): List<Menu>? {
    Log.d(TAG, "getMenu called")

    val sid = this.sid // Recupera il SID
    if (sid == null) {
        Log.e(TAG, "SID is not set.")
        return null
    }

    val url = "$BASE_URL/menu"
    val queryParams = mapOf("lat" to lat, "lng" to lng, "sid" to sid)

    return try {
        // Effettua la richiesta GET
        val httpResponse = genericRequest(url, HttpMethod.GET, queryParams)

        Log.d(TAG, "HTTP Response Status: ${httpResponse.status.value}")
        Log.d(TAG, "HTTP Response Body: ${httpResponse.bodyAsText()}")

        // Parsing della risposta
        val result: List<Menu> = httpResponse.body()
        Log.d(TAG, "Parsed Menu List: $result")
        result
    } catch (e: Exception) {
        Log.e(TAG, "Error during getMenu: ${e.message}")
        null
    }
}

suspend fun getImage(): String? {
    Log.d(TAG, "getImage called")

    val sid = this.sid // Recupera il SID
    val mid = 21 // ID del menu hardcoded

    if (sid == null) {
        Log.e(TAG, "SID is not set.")
        return null
    }

    val url = "$BASE_URL/menu/$mid/image"
    val queryParams = mapOf("sid" to sid)

    return try {
        // Effettua la richiesta GET
        val httpResponse = genericRequest(url, HttpMethod.GET, queryParams)

        Log.d(TAG, "HTTP Response Status: ${httpResponse.status.value}")
        Log.d(TAG, "HTTP Response Body: ${httpResponse.bodyAsText()}")

        // Parsing della risposta
        val response = httpResponse.body<Map<String, String>>()
        response["base64"]
    } catch (e: Exception) {
        Log.e(TAG, "Error during getImage: ${e.message}")
        null
    }
}

suspend fun putUser(uid: Int, user: User): UserResponse? {
    Log.d(TAG, "putUser called for uid: $uid")

    val sid = this.sid // Recupera il SID
    if (sid == null) {
        Log.e(TAG, "SID is not set.")
        return null
    }

    val url = "$BASE_URL/user/$uid"
    val bodyParams = mapOf(
        "firstName" to user.firstName,
        "lastName" to user.lastName,
        "cardFullName" to user.cardFullName,
        "cardNumber" to user.cardNumber,
        "cardExpireMonth" to user.cardExpireMonth,
        "cardExpireYear" to user.cardExpireYear,
        "cardCVV" to user.cardCVV,
        "sid" to sid
    )

    return try {
        // Effettua la richiesta PUT
        val httpResponse = genericRequest(url, HttpMethod.PUT, requestBody = bodyParams)

        Log.d(TAG, "HTTP Response Status: ${httpResponse.status.value}")
        Log.d(TAG, "HTTP Response Body: ${httpResponse.bodyAsText()}")

        // Parsing della risposta
        val result: UserResponse = httpResponse.body()
        Log.d(TAG, "Parsed UserResponse: $result")
        result
    } catch (e: Exception) {
        Log.e(TAG, "Error during putUser: ${e.message}")
        null
    }
}


  



}

