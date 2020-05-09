package ru.ifmo.kirmanak.elasticappclient.kubernetes

import io.kubernetes.client.openapi.models.V1Pod
import ru.ifmo.kirmanak.elasticappclient.AppClientException
import ru.ifmo.kirmanak.elasticappclient.AppInstance
import ru.ifmo.kirmanak.elasticappclient.kubernetes.models.MetricsV1Beta1PodMetrics
import java.util.concurrent.atomic.DoubleAdder

internal data class KubernetesInstance(
    private val name: String,
    private val CPULoad: Double,
    private val RAMLoad: Double
) : AppInstance {

    companion object {
        fun create(pod: V1Pod, client: KubernetesClient): KubernetesInstance? {
            val name = pod.metadata?.name ?: throw AppClientException("Pod name or whole metadata is unknown")
            val cpuLoad = getUsage(name, "cpu", client) ?: return null
            val ramLoad = getUsage(name, "memory", client) ?: return null

            return KubernetesInstance(name, cpuLoad, ramLoad)
        }

        private fun getUsage(podName: String, metricName: String, client: KubernetesClient): Double? {
            val podContainers = getPodMetrics(podName, client)?.containers

            return podContainers?.fold(DoubleAdder()) { acc, container ->
                val usage = container.usage?.get(metricName)?.number
                    ?: throw AppClientException("Usage of \"$metricName\" was not found for container \"${container.name}\"")
                acc.add(usage.toDouble())
                acc
            }?.toDouble()
        }

        private fun getPodMetrics(podName: String, client: KubernetesClient): MetricsV1Beta1PodMetrics? {
            val allMetrics = client.getMetricsPerPod().items ?: return null

            for (item in allMetrics) {
                if (item.metadata?.name == podName)
                    return item
            }

            return null
        }
    }

    override fun getCPULoad() = CPULoad

    override fun getRAMLoad() = RAMLoad

    override fun getName() = name
}