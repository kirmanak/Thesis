package ru.ifmo.kirmanak.elasticappclient.kubernetes.models

import io.kubernetes.client.openapi.models.V1ObjectMeta

/*
{
    "metadata": {
        "name": "nginx-deployment-54f57cf6bf-vrf9h",
        "namespace": "default",
        "selfLink": "/apis/metrics.k8s.io/v1beta1/namespaces/default/pods/nginx-deployment-54f57cf6bf-vrf9h",
        "creationTimestamp": "2020-01-08T12:35:52Z"
    },
    "timestamp": "2020-01-08T12:35:00Z",
    "window": "1m0s",
    "containers": []
}
*/
data class MetricsV1Beta1PodMetrics(
    val metadata: V1ObjectMeta?,
    val containers: List<MetricsV1Beta1Container>?
)