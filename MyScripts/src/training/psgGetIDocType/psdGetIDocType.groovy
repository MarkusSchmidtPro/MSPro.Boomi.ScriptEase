package training.psgGetIDocType

import com.boomi.execution.ExecutionUtil

final String SCRIPT_NAME = "SetIDocType"

/* **************************************************************************
    Set a DDP for an IDoc Name
        
    IN : [Describe inbound arguments]
    OUT: [Describe outbound arguments]
    ------------------------------------------------
    09.10.2024  mspro -   Created
    Template v0.2.1
************************************************************************** */

final String _wellKnownTypes = "WMMBID02,ZTOUR02"

final _logger = ExecutionUtil.getBaseLogger()
//_logger.handlers.each { it.setFormatter(new DefaultFormatter()) }

_logger.info('>>> Script start i' + SCRIPT_NAME)
_logger.fine('>>> Script start f' + SCRIPT_NAME)
_logger.finer('>>> Script start r' + SCRIPT_NAME)
_logger.finest('>>> Script start st' + SCRIPT_NAME)

int docCount = dataContext.getDataCount()
_logger.fine("In-Document Count=" + docCount)

long start = System.currentTimeMillis()

for (int docNo = 0; docNo < docCount; docNo++) {
    final String textDoc = _getTextDocument(dataContext, docNo)
    final Properties props = dataContext.getProperties(docNo)

    // *********** Document related functionality ************

    try {

        def idoc = new XmlSlurper().parseText(textDoc)
        String idocType     = idoc.IDOC.EDI_DC40.IDOCTYP as String

        if( !_wellKnownTypes.contains(idocType ))
            throw new Exception( "Invalid IDoc Type $idocType")

        _setDDP(props, "DDP_IDocType", idocType)
    }
    catch( Exception ex1) {
        _setDDP(props, "DDP_Error", ex1.message)
        // thrown in process
    }

    // ******** end of Document related functionality ********

    _setTextDocument(dataContext, textDoc, props)
}

// Your process related code (process properties etc.) here
// ..
long elapsedTime = System.currentTimeMillis() - start
_logger.fine( "Elapsed time: ${elapsedTime} ms")


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


static String _setDDP(Properties docProperties, String propertyName, String value) {
    final String userDefinedPropertyBase = 'document.dynamic.userdefined.'
    docProperties.setProperty(userDefinedPropertyBase + propertyName, value)
}

/*
class DefaultFormatter extends java.util.logging.Formatter {
    @Override
    String format(LogRecord record) {
        return "${record.instant} | ${record.level}[${record.longThreadID}] | ${record.message}\n"
    }
}*/
