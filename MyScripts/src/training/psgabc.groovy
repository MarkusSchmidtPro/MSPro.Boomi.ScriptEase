package training

import com.boomi.execution.ExecutionUtil

final String SCRIPT_NAME = "abc"

/* **************************************************************************
    def
        
    IN : [Describe inbound arguments]
    OUT: [Describe outbound arguments]
    ------------------------------------------------
    19.11.2024  msc -   Created
    Template v0.2.1
************************************************************************** */

// fine=DEBUG, info=INFO, warning=WARNING, severe=SEVERE
final _logger = ExecutionUtil.getBaseLogger()
_logger.fine('>>> Script start ' + SCRIPT_NAME)

try {
    int docCount = dataContext.getDataCount()
    _logger.fine("In-Document Count=" + docCount)

    for (int docNo = 0; docNo < docCount; docNo++) {
        final String textDoc = _getTextDocument(dataContext, docNo)
        final Properties props = dataContext.getProperties(docNo)

        // *********** Document related functionality ************
        // Your document related code here ...
        // ******** end of Document related functionality ********

        _setTextDocument(dataContext, textDoc, props)
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

private static String _getTextDocument(dataContext, int docNo) {
    InputStream documentStream = dataContext.getStream(docNo)
    return documentStream.getText("UTF-8")
}

private static void _setTextDocument(dataContext, String value, Properties props) {
    InputStream newDocumentStream = new ByteArrayInputStream(value.getBytes("UTF-8"))
    dataContext.storeStream(newDocumentStream, props)
}
