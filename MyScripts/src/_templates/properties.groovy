package _templates

import com.boomi.execution.ExecutionUtil

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


/** Pass through all documents. */
private void documentsPassThrough() {

    for (int docNo = 0; docNo < dataContext.getDataCount(); docNo++) {
        InputStream documentStream = dataContext.getStream(docNo)
        Properties dynDocProperties = dataContext.getProperties(docNo)
        // *********** Script functionality ************
        // ******** end of Script functionality ********
        dataContext.storeStream(documentStream, dynDocProperties)
    }
}
