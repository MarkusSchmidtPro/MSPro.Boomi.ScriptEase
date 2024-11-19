package templatesSource
import com.boomi.execution.ExecutionUtil

final String SCRIPT_NAME = "${Plain_BoomiScriptName}"

/* **************************************************************************
    ${SingleLineDescription}
        
    IN : [Describe inbound arguments]
    OUT: [Describe outbound arguments]
    ------------------------------------------------
    ${DATE}  ${Author} -   Created
    Template v0.2.1
************************************************************************** */

// fine=DEBUG, info=INFO, warning=WARNING, severe=SEVERE
final _logger = ExecutionUtil.getBaseLogger()
_logger.fine('>>> Script start ' + SCRIPT_NAME)

try {
    int docCount = dataContext.getDataCount()
    _logger.fine("In-Document Count=" + docCount)

    for (int docNo = 0; docNo < docCount; docNo++) {
        final String textDoc = _getTextDocument( docNo)
        final Properties props = dataContext.getProperties( docNo)

        // *********** Document related functionality ************
        // Your document related code here ...
        // ******** end of Document related functionality ********

        _setTextDocument( textDoc, props)
    }
	
	// Your process related code (process properties etc.) here
	// ..
}
catch (Exception e) {
    _logger.severe(e.message)
    throw e
}



// =================================================
// -------------------- LOCALS ---------------------
// =================================================

// region Document as Text

/** Get a a document from the [dataContext] (input stream).
 * @param docNo Document number : 0 ... dataContext.getDataCount() -1
 * @return The document as a String, UTF-8 decoded from the input stream.
 */
private String _getTextDocument( int docNo) {
    InputStream documentStream = dataContext.getStream(docNo)
    return documentStream.getText("UTF-8")
}


/** Write a (text) document back into the output stream.
 * @param value The document text as String.
 * @param props The document's dynamic properties as the have been read
 *                using `final Properties props = dataContext.getProperties(docNo)`.
 */
private void _setTextDocument( String value, Properties props) {
    InputStream newDocumentStream = new ByteArrayInputStream(value.getBytes("UTF-8"))
    dataContext.storeStream(newDocumentStream, props)
}

// endregion

// region Dynamic Document Properties

/** Get a Dynamic Document Property.
 *
 * @param docProperties The document properties [props] as they were read using 
 *                      <code>Properties props = dataContext.getProperties(docNo)</code>
 * @param propertyName The document property name.
 * @param defaultValue If default is null (not provided), an exception is thrown when the property is not set.
 *                      Otherwise, the [defaultValue] is returned in case it does not exist (is empty).
 *                      Whitespaces are valid characters and a whitespaces string is not empty!
 * @return The property value (or [defaultValue])
 */
private static String _getDDP(Properties docProperties, String propertyName, String defaultValue = null) {
    final String userDefinedPropertyBase = 'document.dynamic.userdefined.'
    String v = docProperties.getProperty(userDefinedPropertyBase + propertyName)
    if (v == null || v.isEmpty()) {
        // Default handler
        if (defaultValue == null) throw new Exception("Mandatory " + propertyName + " not set.")
        v = defaultValue
    }
    return v
}

/** Set (create or update) a Dynamic Document Property.
 *
 * @param docProperties The document properties [props] as they were read using 
 *                      <code>Properties props = dataContext.getProperties(docNo)</code>
 * @param propertyName The document property name.
 * @param value
 */
private static void _setDDP(Properties docProperties, String propertyName, String value) {
    final String userDefinedPropertyBase = 'document.dynamic.userdefined.'
    docProperties.setProperty(userDefinedPropertyBase + propertyName, value)
}
// endregion


// region Dynamic Process Properties

/** Get a dynamic process property.
 *
 * @param propertyName
 * @param default If default is null, an exception is thrown when the property is not set.
 *                      Otherwise, the default is returned in case it does not exist (is empty).
 *                      Whitespaces are valid characters and a whitespaces string is not empty!
 * @return The property value (or default)
 */
private static String _getDPP(String propertyName, String defaultValue = null) {
    String v = ExecutionUtil.getDynamicProcessProperty(propertyName)
    if (v == null || v.isEmpty()) {
        // Default handler
        if (defaultValue == null) throw new Exception("Mandatory " + propertyName + " not set.")
        v = defaultValue
    }
    return v
}

/** Set (create or update) a Dynamic Process Property.
 *
 * @param propertyName
 * @param value
 */
static void _setDPP(String propertyName, String value) {
    ExecutionUtil.setDynamicProcessProperty(propertyName, value, false)
}

// endregion 