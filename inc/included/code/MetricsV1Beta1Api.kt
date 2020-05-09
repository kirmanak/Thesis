package ru.ifmo.kirmanak.elasticappclient.kubernetes

import com.google.gson.reflect.TypeToken
import io.kubernetes.client.openapi.ApiClient
import io.kubernetes.client.openapi.ApiResponse
import okhttp3.Call
import ru.ifmo.kirmanak.elasticappclient.AppClientException
import ru.ifmo.kirmanak.elasticappclient.kubernetes.models.MetricsV1Beta1PodMetricsList

private const val API_PATH = "/apis/metrics.k8s.io/v1beta1"
private val AVAILABLE_ACCEPT_HEADERS = arrayOf(
    "application/json",
    "application/yaml",
    "application/vnd.kubernetes.protobuf",
    "application/json;stream=watch",
    "application/vnd.kubernetes.protobuf;stream=watch"
)

internal class MetricsV1Beta1Api(private var api: ApiClient) {

    fun getPodMetrics(namespace: String? = null): MetricsV1Beta1PodMetricsList {
        val path = if (namespace === null) {
            "pods"
        } else {
            "namespaces/$namespace/pods"
        }

        val call = buildGET("$API_PATH/$path")
        val type = object : TypeToken<MetricsV1Beta1PodMetricsList>() {}

        return execute(call, type).data
    }

    private fun buildGET(url: String): Call {
        val headers: MutableMap<String, String> = HashMap()

        api.selectHeaderAccept(AVAILABLE_ACCEPT_HEADERS)?.let { headers["Accept"] = it }
        api.selectHeaderContentType(emptyArray())?.let { headers["Content-Type"] = it }

        val authNames = arrayOf("BearerToken")
        return api.buildCall(
            url, "GET", emptyList(), emptyList(), null, headers,
            emptyMap(), emptyMap(), authNames, null
        )
    }

    private fun <T> execute(call: Call, typeToken: TypeToken<T>): ApiResponse<T> {
        val type = typeToken.type

        try {
            return api.execute(call, type)
        } catch (e: Exception) {
            throw AppClientException(e)
        }
    }
}