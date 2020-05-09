package ru.ifmo.kirmanak.elasticappclient.kubernetes.models

import io.kubernetes.client.openapi.models.V1ObjectMeta

/*
{
  "kind": "PodMetricsList",
  "apiVersion": "metrics.k8s.io/v1beta1",
  "metadata": {
    "selfLink": "/apis/metrics.k8s.io/v1beta1/pods"
  },
  "items": []
*/
data class MetricsV1Beta1PodMetricsList(
    val kind: String?,
    val apiVersion: String?,
    val metadata: V1ObjectMeta?,
    val items: List<MetricsV1Beta1PodMetrics>?
)