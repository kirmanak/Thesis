package ru.ifmo.kirmanak.elasticappclient.opennebula

import org.opennebula.client.Client
import org.opennebula.client.template.Template
import org.opennebula.client.vm.VirtualMachine
import org.opennebula.client.vm.VirtualMachinePool
import org.opennebula.client.vmgroup.VMGroup
import org.w3c.dom.Node
import ru.ifmo.kirmanak.elasticappclient.AppClient
import ru.ifmo.kirmanak.elasticappclient.AppClientException
import ru.ifmo.kirmanak.elasticappclient.AppInstance
import kotlin.math.min

open class OpenNebulaClient(
    private val client: Client, private val groupId: Int, private val roleId: Int, private val templateId: Int
) : AppClient {

    override fun getAppInstances(): Array<AppInstance> =
        getVirtualMachines().map { OpenNebulaInstance(it) }.toTypedArray()

    override fun scaleInstances(count: Int) {
        if (count > 0) addInstances(count)
        else if (count < 0) removeInstances(-count)
    }

    private fun getVirtualMachines(): List<VirtualMachine> {
        val pool = VirtualMachinePool(client)
        throwIfError(pool.info())
        val vmIDs = getVMIdentifiers()

        return pool.filter { vmIDs.contains(it.id()) }
    }

    private fun addInstances(count: Int) {
        val template = Template(templateId, client)
        val roleName = getString(getRole(), "NAME")
        val groupTemplate = "VMGROUP = [ VMGROUP_ID = \"$groupId\", ROLE = \"$roleName\" ]"
        for (i in 0 until count) {
            val response = template.instantiate("", false, groupTemplate, false)
            throwIfError(response)
        }
    }

    private fun removeInstances(count: Int) {
        val list = getVirtualMachines()

        val toRemove = min(count, list.size)
        if (toRemove == 0) return

        list.take(toRemove).forEach { throwIfError(it.terminate()) }
    }

    private fun getRole(): Node =
        findRole() ?: throw AppClientException("Failed to find role with id $roleId in VM group $groupId")

    private fun getVMIdentifiers(): Set<Int> {
        val role = getRole()
        val vmList = getString(role, "VMS")
        return vmList.split(",").filter { it.isNotBlank() }.map { it.toInt() }.toSortedSet()
    }

    private fun findRole(): Node? {
        val group = VMGroup(groupId, client)

        val root = getRootElement(group.info())
        val roleList = getNodeList(root, "ROLES/ROLE")
        for (i in 0 until roleList.length) {
            val item = roleList.item(i)
            val id = getNumber(item, "ID").toInt()
            if (id == roleId)
                return item
        }

        return null
    }
}
