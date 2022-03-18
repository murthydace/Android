package com.local.growkart.api

import android.content.Context
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URLConnection

/**
 * OkHttp3 interceptor which provides a mock response from local resource file.
 */
class MockResponseInterceptor : Interceptor {
    private var context: Context
    private var scenario: String? = null

    constructor(context: Context, scenario: String?) {
        this.context = context.applicationContext
        this.scenario = scenario
    }

    constructor(context: Context) {
        this.context = context.applicationContext
    }

    fun setScenario(scenario: String?) {
        this.scenario = scenario
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        // Get resource ID for mock response file.
        var fileName = getFilename(chain.request(), scenario)
        var resourceId = getResourceId(fileName)
        if (resourceId == 0) {
            // Attempt to fallback to default mock response file.
            fileName = getFilename(chain.request(), null)
            resourceId = getResourceId(fileName)
            if (resourceId == 0) {
                throw IOException("Could not find res/raw/$fileName")
            }
        }

        // Get input stream and mime type for mock response file.
        val inputStream = context.resources.openRawResource(resourceId)
        var mimeType = URLConnection.guessContentTypeFromStream(inputStream)
        if (mimeType == null) {
            mimeType = "application/json"
        }

        // Build and return mock response.
        return Response.Builder()
            .addHeader("content-type", mimeType)
            .body(toByteArray(inputStream).toResponseBody(mimeType.toMediaTypeOrNull()))
            .code(200)
            .message("Mock response from res/raw/$fileName")
            .protocol(Protocol.HTTP_1_0)
            .request(chain.request())
            .build()
    }

    @Throws(IOException::class)
    private fun getFilename(request: Request, scenario: String?): String {
        val requestedMethod = request.method
        val prefix = if (scenario == null) "" else scenario + "_"
        var filename = prefix + requestedMethod + request.url.toUrl().path
        filename = filename.replace("/", "_").replace("-", "_").toLowerCase()
        return filename
    }

    private fun getResourceId(filename: String): Int {
        return context.resources.getIdentifier(filename, "raw", context.packageName)
    }

    companion object {
        private const val BUFFER_SIZE = 1024 * 4
        @Throws(IOException::class)
        private fun toByteArray(`is`: InputStream): ByteArray {
            val output = ByteArrayOutputStream()
            return try {
                val b = ByteArray(BUFFER_SIZE)
                var n = 0
                while (`is`.read(b).also { n = it } != -1) {
                    output.write(b, 0, n)
                }
                output.toByteArray()
            } finally {
                output.close()
            }
        }
    }
}