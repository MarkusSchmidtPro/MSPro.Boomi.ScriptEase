package processScript.process_log

import com.boomi.execution.ExecutionUtil

final String SCRIPT_NAME = "psgTrace"

/**
 * A PROCESS script that pretty-prints (write) the current dataContext
 * and all documents and dynamic document properties to the trace 
 * (see Debug Windows / Boomi Reporting). 
 *
 * Parameters   : n/a
 * Returns      : Input documents pass through
 * ------------------------------------------------------------------------
 * 2023-12-19   msc -   Refactored
 * 2022-06-26   msc -   Created
 */

_logger = ExecutionUtil.getBaseLogger()
_logger.info('>>> Start Script ' + SCRIPT_NAME)

final String userDefinedPropertyBase = 'document.dynamic.userdefined.'


try {
    int docCount = dataContext.getDataCount()
    _logger.info("Document Count=$docCount")

    for (int docNo = 0; docNo < docCount; docNo++) {
        String textDoc = _getTextDocument(dataContext, docNo)
        Properties props = dataContext.getProperties(docNo)

        // *********** Script functionality ************
        // Log document content and all DDPs

        _logger.info("Doc[$docNo]=$textDoc")
        for (def ddp in props) _logger.info("-- ${ddp.key}=\"${ddp.value}\"")
        
        // Don't forget to use the userDefinedPropertyBase as the root path to the DDPs
        String ddpV1 = props[ userDefinedPropertyBase + "DDP_V1"]
        if( ddpV1 == null) {
            // In a real application you will want to throw an exception 
            // if mandatory DDP are not defined. YOu throw that exception because the script
            // cannot do anything meaningful without the property. 
            // ---- throw new Exception( "DDP_V1 is not defined!")
            
            // If you write a message to the logs, this will appear, of course, in the Boomi logs, too.
            // However, the script (and also the process) will continue without notice that
            // something went wrong.
            _logger.severe("DDP_V1 is not defined!")
        }
        else _logger.info("DDP_V1='$ddpV1'") 
       

        // ******** end of Script functionality ********
        _setTextDocument(dataContext, textDoc, props)
    }

    _logger.info('<<< End Script')
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