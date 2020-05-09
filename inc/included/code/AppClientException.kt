package ru.ifmo.kirmanak.elasticappclient

class AppClientException(override val message: String) : Exception(message) {
    constructor(throwable: Throwable) : this(throwable.toString()) {
        addSuppressed(throwable)
    }
}