// BoomiConsole_Header_Start
// Current   : https://platform.boomi.com/AtomSphere.html#build;accountId=abcfinancegmbh-5BZDOF;components=838a0300-8bdb-4ad2-9502-549b9cf0e79b
// Base [010]: https://platform.boomi.com/AtomSphere.html#build;accountId=abcfinancegmbh-5BZDOF;components=838a0300-8bdb-4ad2-9502-549b9cf0e79b~10
// BoomiConsole_Header_End
package processScript.datahub

import com.boomi.execution.ExecutionUtil


final String SCRIPT_NAME = "AttributesToProperties"

/* **************************************************************************
    Copy Datahub Response Attributes to dynamic document properties.
        
    IN : Any Datahub Response 
        <... createddate="02-16-2026T09:35:50.000+0000" 
             grid="59c7cd73-0d25-4a0a-a86a-9b69f7745b86" 
             updateddate="02-17-2026T09:06:33.000+0000" 
             source="NAVBC">
    OUT: 
        DDP_Res_CreatedDate
        DDP_Res_Grid
        DDP_Res_UpdateDate
        DDP_Res_EndDate
        DDP_Res_Source
    ------------------------------------------------
    2026-02-18  MSPro -   Logs
    2026-02-17  MSPro -   Created
    Template v0.2.2
************************************************************************** */

// config=DEBUG, info=INFO, warning=WARNING, severe=SEVERE
logger = _logger = ExecutionUtil.getBaseLogger()
_logger.config('>>> Script start ' + SCRIPT_NAME)

try {
    int docCount = dataContext.getDataCount()
    _logger.config("In-Document Count=" + docCount)

    for (int docNo = 0; docNo < docCount; docNo++) {
        final String textDoc = _getTextDocument(docNo)
        final Properties props = dataContext.getProperties(docNo)

        // _logger.config( textDoc)
        
        // *********** Document related functionality ************
        def root = new XmlParser().parseText(textDoc)

        // createddate="02-16-2026T09:35:50.000+0000" 
        // grid="59c7cd73-0d25-4a0a-a86a-9b69f7745b86" 
        // updateddate="02-17-2026T09:06:33.000+0000" 
        // source="NAVBC">

        _setDDP( props, "DDP_Res_CreatedDate", root.@createddate as String)
        _setDDP( props, "DDP_Res_Grid", root.@grid as String)
        _setDDP( props, "DDP_Res_UpdateDate", root.@updateddate as String)
        _setDDP( props, "DDP_Res_EndDate", root.@enddate != null ? root.@enddate : "")  // can be null
        _setDDP( props, "DDP_Res_Source", root.@source as String)

        // This represents the GRID, too.
        //_setDDP( props, "DDP_Res_EntityId", root.id.text() as String)


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

String _setDDP(Properties docProperties, String propertyName, String value) {
    final String userDefinedPropertyBase = 'document.dynamic.userdefined.'
    docProperties.setProperty(userDefinedPropertyBase + propertyName, value)
    _logger.info( "${propertyName}=${value}")
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

