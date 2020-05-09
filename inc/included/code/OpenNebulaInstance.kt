package ru.ifmo.kirmanak.elasticappclient.opennebula

import org.opennebula.client.vm.VirtualMachine
import ru.ifmo.kirmanak.elasticappclient.AppClientException
import ru.ifmo.kirmanak.elasticappclient.AppInstance

internal class OpenNebulaInstance(vm: VirtualMachine) : AppInstance {
    private val name: String = vm.name ?: throw AppClientException("Unknown vm name")
    private val cpuLoad = getUsage("CPU", vm) / 100
    private val memoryLoad = getUsage("MEMORY", vm) * 1024

    override fun getCPULoad() = cpuLoad

    override fun getRAMLoad() = memoryLoad

    override fun getName() = name

    private fun getUsage(resource: String, vm: VirtualMachine): Double {
        val root = getRootElement(vm.info())
        return getNumber(root, "MONITORING/$resource")
    }

    override fun toString(): String {
        return "OpenNebulaNode(name='$name', cpuLoad=$cpuLoad, memoryLoad=$memoryLoad)"
    }
}
