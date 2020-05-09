package ru.ifmo.kirmanak.elasticappclient

/**
 * Interface to access elastic application working in a virtualized infrastructure.
 */
interface AppClient {
    /**
     * Requests information about currently working application instances from infrastructure provider.
     */
    @Throws(AppClientException::class)
    fun getAppInstances(): Array<AppInstance>

    /**
     * Scales application instances count by {@param count}.
     * Instances are added if {@param count} is positive and removed if it is negative.
     * If {@param count} is zero nothing will be done.
     */
    @Throws(AppClientException::class)
    fun scaleInstances(count: Int)
}