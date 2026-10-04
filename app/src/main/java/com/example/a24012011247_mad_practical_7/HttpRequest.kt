package com.example.a24012011247_mad_practical_7

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HttpRequest {

    fun makeServiceCall(reqUrl: String, token: String): String? {

        var connection: HttpURLConnection? = null

        return try {
            val url = URL(reqUrl)
            connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
            connection.setRequestProperty("Authorization", "Bearer $token")
            connection.setRequestProperty("Content-Type", "application/json")
            connection.connectTimeout = 15000
            connection.readTimeout = 15000
            android.util.Log.d("HttpRequest", "Token length: ${token.length}")

            val responseCode = connection.responseCode

            android.util.Log.d("HttpRequest", "Response Code: $responseCode")

            val stream = if (responseCode in 200..299) {
                connection.inputStream
            } else {
                connection.errorStream
            }

            val reader = BufferedReader(InputStreamReader(stream))
            val response = reader.readText()
            reader.close()

            android.util.Log.d("HttpRequest", "Response: $response")

            if (responseCode in 200..299) {
                response
            } else {
                null
            }

        } catch (e: Exception) {
            android.util.Log.e("HttpRequest", "Request failed", e)
            null
        } finally {
            connection?.disconnect()
        }
    }
}