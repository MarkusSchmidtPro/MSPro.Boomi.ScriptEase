package processScript.xmlToJson

import com.boomi.execution.ExecutionUtil
import groovy.json.JsonOutput

final String SCRIPT_NAME = "xmlToJson"

/* **************************************************************************
    COnvert an XML document into a JSON document.
        
    IN : [Describe inbound arguments]
    OUT: [Describe outbound arguments]
    ------------------------------------------------
    01.12.2024  mspro -   Created
    Template v0.2.2
************************************************************************** */

// fine=DEBUG, info=INFO, warning=WARNING, severe=SEVERE
final _logger = ExecutionUtil.getBaseLogger()
_logger.fine('>>> Script start ' + SCRIPT_NAME)

def xs = new XmlSlurper()

try {
    int docCount = dataContext.getDataCount()
    _logger.fine("In-Document Count=" + docCount)

    for (int docNo = 0; docNo < docCount; docNo++) {
        final String textDoc = _getTextDocument(docNo)
        final Properties props = dataContext.getProperties(docNo)

        // *********** Document related functionality ************
        // Parse XML


        def xml = xs.parseText(textDoc)

        // Convert the parsed XML to JSON

        def map = xmlToMap(xml)
        textDoc = JsonOutput.toJson(map)

        // ******** end of Document related functionality ********

        _setTextDocument(textDoc, props)
    }

    // Your process related code (process properties etc.) here
    // ..
}
catch (Exception e) {
    _logger.severe(e.message)
    throw e
}

// Recursive function to convert XML to Map
// ChatGPT: groovy xml to map
def xmlToMap(node) {
    Map result = [:]

    node.children().each { child ->
        // Check if the child has nested elements
        if (child.children().size() > 0) {
            // Recursive call for nested elements
            def childMap = xmlToMap(child)
            if (result[child.name()]) {
                // Handle multiple elements with the same name (convert to list)
                if (!(result[child.name()] instanceof List)) {
                    result[child.name()] = [result[child.name()]]
                }
                result[child.name()] << childMap
            } else {
                result[child.name()] = childMap
            }
        } else {
            // Handle text-only nodes
            if (result[child.name()]) {
                // Convert to list if multiple text nodes with the same name
                if (!(result[child.name()] instanceof List)) {
                    result[child.name()] = [result[child.name()]]
                }
                result[child.name()] << child.text()
            } else {
                result[child.name()] = child.text()
            }
        }
    }
    return result
}

// =================================================
// -------------------- LOCALS ---------------------
// =================================================

// region Document as Text

/** Get a a document from the [dataContext] (input stream).
 * @param docNo Document number : 0 ... dataContext.getDataCount() -1
 * @return The document as a String, UTF-8 decoded from the input stream.
 */
private String _getTextDocument(int docNo) {
    InputStream documentStream = dataContext.getStream(docNo)
    return documentStream.getText("UTF-8")
}


/** Write a (text) document back into the output stream.
 * @param value The document text as String.
 * @param props The document's dynamic properties as the have been read
 *                using `final Properties props = dataContext.getProperties(docNo)`.
 */
private void _setTextDocument(String value, Properties props) {
    InputStream newDocumentStream = new ByteArrayInputStream(value.getBytes("UTF-8"))
    dataContext.storeStream(newDocumentStream, props)
}

// endregion

