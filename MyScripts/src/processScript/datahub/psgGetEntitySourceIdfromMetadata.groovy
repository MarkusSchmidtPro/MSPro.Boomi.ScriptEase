package processScript.datahub

import com.boomi.execution.ExecutionUtil
import groovy.ui.Console

final String SCRIPT_NAME = "GetEntitySourceIdfromMetadata"

/* **************************************************************************
    Get the sourceId of an entity from Golden Record Metadata
        
    IN :
    * DPP_SourceId = "EFX" 
    * PROFILE
        <Record recordId="59c7cd73-0d25-4a0a-a86a-9b69f7745b86">
          <links>
            <link source="NAVBC" entityId="NAV_0001" establishedDate="2026-02-16T09:35:50Z"/>
            <link source="EFX" entityId="EFX-1" establishedDate="2026-02-17T08:43:44Z"/>
          </links>
        </Record> 
    
    OUT: 
    WARINING: We use a Process Property to make it usable in the process.
    * DPP_SourceEntityId_DH = EFX-1
    
    ------------------------------------------------
    2026-02-17  MSPro -   Created
    Template v0.2.2
************************************************************************** */

// fine=DEBUG, info=INFO, warning=WARNING, severe=SEVERE
final _logger = ExecutionUtil.getBaseLogger()
_logger.config('>>> Script start ' + SCRIPT_NAME)

String sourceId = _getDPP( "DPP_SourceId")

try {
    int docCount = dataContext.getDataCount()
    _logger.config("In-Document Count=" + docCount)

    for (int docNo = 0; docNo < docCount; docNo++) {
        final String textDoc = _getTextDocument(docNo)
        final Properties props = dataContext.getProperties(docNo)

        // *********** Document related functionality ************
        String sourceEntityId = ""
        def root = new XmlParser().parseText(textDoc)
        def links = root.links.link
        for (final link in links) {
             if( link.@source == sourceId)
             {
                 sourceEntityId = link.@entityId
                 break;
             }
        }
        _setDDP( props, "DPP_SourceEntityId_DH", sourceEntityId)
        
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

// =================================================
// -------------------- LOCALS ---------------------
// =================================================

private static String _getDPP(String propertyName, String defaultValue = null) {
    String v = ExecutionUtil.getDynamicProcessProperty(propertyName)
    if (v == null || v.isEmpty()) {
        // Default handler
        if (defaultValue == null) throw new Exception("Mandatory " + propertyName + " not set.")
        v = defaultValue
    }
    return v
}


static String _setDDP(Properties docProperties, String propertyName, String value) {
    final String userDefinedPropertyBase = 'document.dynamic.userdefined.'
    docProperties.setProperty(userDefinedPropertyBase + propertyName, value)
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

