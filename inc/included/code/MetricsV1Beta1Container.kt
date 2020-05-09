package ru.ifmo.kirmanak.elasticappclient.kubernetes.models

import io.kubernetes.client.custom.Quantity

/*
{
    "name": "nginx",
    "usage": {
        "cpu": "0",
        "memory": "2392Ki"
    }
}
*/

data class MetricsV1Beta1Container(
    val name: String?,
    val usage: Map<String, Quantity>?
)