package processScript.demo

import com.boomi.execution.ExecutionUtil
import groovy.json.JsonOutput
import groovy.json.JsonSlurper


/// *************** COPY AND PASTE FROM HERE *****************

final String SCRIPT_NAME = "processScript.demo.psgDemo"

/* **************************************************************************
    A process script that demonstrates (and tests) 
    * documents, 
    * dynamic document properties,
    * dynamic process properties,
    * process properties
    * and the execution context. 
     
    IN: 
        * JSON Document 
            - containing fields [firstname] and [lastname]  
        * [DDP_IntValue] of type int
        * [DPP_IntValue] of type int
        * A process property with the given Ids( see below)
            
    The functionality is simple:
	    * increment each [DDP_IntValue]
	    * increment the [DPP_IntValue] and process property
	    
	Add a new field [fullname] to the JSON document that 
	concatenates [lastname], [firstname]
	
    ------------------------------------------------
    27.04.2024  mspro   -   Created
************************************************************************** */

// Use a logger to write information to process reporting
final _logger = ExecutionUtil.getBaseLogger()
_logger.info('>>> Script start ' + SCRIPT_NAME)

def js = new JsonSlurper()

try {
	// The name of the dynamic document property that will be incremented
	final String DDP_PROP_NAME = "DDP_IntValue"
	
	// ---------------------------------
	// Process documents
	// ---------------------------------
	_logger.fine("In-Document Count=${dataContext.getDataCount()}")
	for (int docNo = 0; docNo < dataContext.getDataCount(); docNo++) {
		final String textDoc = _getTextDocument(dataContext, docNo)
		final Properties props = dataContext.getProperties(docNo)

		// *********** Document related functionality ************
		//_logger.info("Doc[${docNo}]='${textDoc}'")       // Log the document itself --> Boomi Trace

		// Increment Dynamic Document Property
		int ddpVal1 = _getDDP(props, DDP_PROP_NAME) as int
		_setDDP(props, DDP_PROP_NAME, ddpVal1 + 1)
		_logger.info("${DDP_PROP_NAME}='${ddpVal1}'")   
		
		// Document is JSON and we want to concatenate firstname and lastname
		Map jsonDoc = js.parseText( textDoc)  as Map
		// to understand the next line, read about JsonSlurper (or XmlSlurper)
		// YOu may want to set a breakpoint there!
		jsonDoc.fullname = "${jsonDoc.lastname}, ${jsonDoc.firstname}" 
				
		// ******** end of Document related functionality ********
		textDocOut = JsonOutput.toJson( jsonDoc)
		_setTextDocument(dataContext, textDocOut, props)
	}

	// ---------------------------------------
	// Process related functionality 
	// runs outside the document loop
	// ---------------------------------------

	// increment DDP_Value and DPP_Value 

	final String DPP_PROP_NAME = "DPP_IntValue"
	int dppValue = _getDPP(DPP_PROP_NAME) as int
	_setDPP(DPP_PROP_NAME, dppValue + 1)
	_logger.info("${DPP_PROP_NAME}='${dppValue}'")

	final String PROCESS_PROPERTY_COMPONENT_ID = "8fb41f63-a988-4778-8cc8-0144f30ace81"
	final String VAL1_ID = "eea9e988-cb14-4a84-ba37-ee455451a741"
	int ppVal1 = ExecutionUtil.getProcessProperty(PROCESS_PROPERTY_COMPONENT_ID, VAL1_ID) as int
	ExecutionUtil.setProcessProperty(PROCESS_PROPERTY_COMPONENT_ID, VAL1_ID, ppVal1 + 1)
}
catch (Exception e) {
	_logger.severe(e.message)
	throw e
}


// =================================================
// -------------------- LOCALS ---------------------
// =================================================

static String _getTextDocument(dataContext, int docNo) {
	InputStream documentStream = dataContext.getStream(docNo)
	return documentStream.getText("UTF-8")
}

static void _setTextDocument(dataContext, String value, Properties props) {
	InputStream newDocumentStream = new ByteArrayInputStream(value.getBytes("UTF-8"))
	dataContext.storeStream(newDocumentStream, props)
}


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

