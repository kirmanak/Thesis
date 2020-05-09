package ru.ifmo.kirmanak.elasticappclient

import io.kubernetes.client.openapi.ApiClient
import org.opennebula.client.Client
import ru.ifmo.kirmanak.elasticappclient.kubernetes.KubernetesClient
import ru.ifmo.kirmanak.elasticappclient.opennebula.OpenNebulaClient

open class AppClientFactory {
    companion object {
        @JvmStatic
        fun getClient(kubeClient: ApiClient, namespace: String, deployment: String): AppClient {
            return KubernetesClient(kubeClient, namespace, deployment)
        }

        @JvmStatic
        fun getClient(openNebulaClient: Client, groupID: Int, roleId: Int, templateId: Int): AppClient {
            return OpenNebulaClient(openNebulaClient, groupID, roleId, templateId)
        }
    }
}