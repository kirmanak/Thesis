package ru.ifmo.kirmanak.elasticappclient.opennebula

import org.opennebula.client.OneResponse
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import ru.ifmo.kirmanak.elasticappclient.AppClientException
import java.io.ByteArrayInputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory


internal val Any.xpath by lazy {
    XPathFactory.newInstance().newXPath()
}

internal val Any.docBuilder by lazy {
    DocumentBuilderFactory.newInstance().newDocumentBuilder()
}

@Throws(AppClientException::class)
internal fun throwIfError(response: OneResponse): OneResponse {
    if (response.isError)
        throw AppClientException(response.errorMessage)
    else
        return response
}

internal fun Any.getRootElement(response: OneResponse): Element {
    val message = throwIfError(response).message
    val stream = ByteArrayInputStream(message.toByteArray())
    return docBuilder.parse(stream).documentElement
}

internal fun Any.getNodeList(node: Node, expression: String): NodeList =
    xpath.evaluate(expression, node, XPathConstants.NODESET) as NodeList

internal fun Any.getNumber(node: Node, expression: String): Double =
    xpath.evaluate(expression, node, XPathConstants.NUMBER) as Double

internal fun Any.getString(node: Node, expression: String): String =
    xpath.evaluate(expression, node, XPathConstants.STRING) as String
