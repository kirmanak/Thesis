package ru.ifmo.kirmanak.elasticappclient

/**
 * Interface to access elastic application instance information.
 * It is either a container or a virtual machine.
 *
 * Be aware: all of the information is cached in object's fields.
 * Instance might not exist by the time data is requested.
 */
interface AppInstance {
    /**
     * CPU load is represented in CPU core usage in the last minute.
     * For instance, if this value is 0.5, then half of a CPU core was used in the minute before data was requested.
     * CPU usage is always absolute value, it is the same on 12-core machine and 36-core machine.
     */
    fun getCPULoad(): Double

    /**
     * RAM usage is represented in bytes of RAM used in the last minute.
     * For example, if this value is 1024 then 1 KiB of RAM was used by this instance in the minute before data was requested.
     * RAM usage is always absolute value, it is the same both on 16 GiB RAM machine and 1 GiB RAM machine.
     */
    fun getRAMLoad(): Double

    /**
     * Name of the instance in terms of virtualized infrastructure provider.
     */
    fun getName(): String
}