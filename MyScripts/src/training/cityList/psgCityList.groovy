package training.cityList

import com.boomi.execution.ExecutionUtil
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

import java.util.logging.Logger

final String SCRIPT_NAME = "CityList"

/* **************************************************************************
    Build a list of cities and their citizens.
    
    See readme.md
    
    IN : [Describe inbound arguments]
    OUT: [Describe outbound arguments]
    ------------------------------------------------
    29.11.2024  mspro -   Created
    Template v0.2.2
************************************************************************** */

// fine=DEBUG, info=INFO, warning=WARNING, severe=SEVERE
final Logger _logger = ExecutionUtil.getBaseLogger()
_logger.fine('>>> Script start ' + SCRIPT_NAME)

def js = new JsonSlurper()

try {
    int docCount = dataContext.getDataCount()
    _logger.fine("In-Document Count=" + docCount)

    // That is where we collect citizens
    // Key: City, Value: [ citizens ]
    Map cityMap = [:]
    for (int docNo = 0; docNo < docCount; docNo++) {
        final String textDoc = _getTextDocument(docNo)
        // not used, here: inbound documents are aggregated into ont output and
        // their Dynamic Document Properties are discarded.
        // final Properties props = dataContext.getProperties(docNo)

        // *********** Document related functionality ************

        // You can omit the "as Map" and IntelliJ will give you the tip
        // that you are implicitly converting Object to Map. I prefer the 
        // explicit conversion by adding "as Map"
        Map jDoc = js.parseText(textDoc) as Map

        String currentCity = jDoc.address.city
        if (!cityMap.containsKey(currentCity)) {
            // yet, unknown city: add empty citizen list
            cityMap[currentCity] = [] // [] shortcut for: new ArrayList()
        }

        // Built the citizen object
        Map citizen = [
                name  : jDoc.name,
                // see below: add if not empty
                //street: jDoc.address.street 
        ]

        // Omit blank or not provided streets
        // isNullOrEmpty is defined down below. 
        // if-statement with one line do not need {}.
        if (!isNullOrEmpty( jDoc.address.street as String))
            citizen.street = jDoc.address.street


        // and add it to the current city's list
        cityMap[currentCity].add(citizen)

        // ******** end of Document related functionality ********
        // only ONE document at the end!!
        //_setTextDocument(textDoc, props)
    }

    // the cityMap is ready and must be sorted, now
    //cityMap = cityMap.sort( { it.key})
    cityMap = cityMap.sort({ Map.Entry<Object, Object> cityEntry -> cityEntry.key })

    // Create  DDP_CityCount
    int cityCount = cityMap.keySet().size()
    
    /* 
        Demoing several list methods.
     */
    List<String> allCitizens =cityMap.values()
            .flatten()
            .collect( {def citizen -> citizen.name})
    assert allCitizens.size() == 4
    
    HashSet<String> uniqueCitizens =allCitizens.toSet()
    assert uniqueCitizens.size() == 3

            // A new document needs new properties !!!!
    def docProps = new Properties()
    _setDDP(docProps, "DDP_CityCount", cityCount as String)

    String outputDoc = JsonOutput.toJson(cityMap)
    //String doc = JsonOutput.prettyPrint(outputDoc)

    // Create the output document with its dynamic document properties
    _setTextDocument(outputDoc, docProps)
}
catch (Exception e) {
    _logger.severe(e.message)
    throw e
}

// =================================================
// -------------------- LOCALS ---------------------
// =================================================

// By JavaBean definition: s.isEmpty() == s.empty.
private static boolean isNullOrEmpty(String s) {
    return null == s || s.empty
}

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

