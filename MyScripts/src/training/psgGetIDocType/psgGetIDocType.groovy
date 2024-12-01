package training.psgGetIDocType

import com.boomi.execution.ExecutionUtil

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.logging.*

final String SCRIPT_NAME = "SetIDocType"

/* **************************************************************************
    The script functionality refers to the IDoc element:
    <WMMBXYEX>
        <IDOC BEGIN="1">
            <EDI_DC40 SEGMENT="1">
                ...
                <IDOCTYP>WMMBID02</IDOCTYP>
     
     --> idoc.IDOC.EDI_DC40.IDOCTYP
    
    It is checked the the IDoc type is a [_wellKnownTypes]. 
    
    In case it is a known IDoc Type, [DDP_IDocType] ist set to [IDOCTYP].
    
    In case it is an unknown IDoc type an Exception is thrown 
    that is then caught and turned into [DDP_Error] - Boomi Script Exception Handler Pattern.
    
    IN : Idoc.Xml
    OUT: pass-through
    ------------------------------------------------
    09.10.2024  mspro -   Created
    Template v0.2.1
************************************************************************** */

final String _wellKnownTypes = "WMMBID02,ZTOUR02"

/*
    This script also demos the use of a 
    ---> customer logging format, which is for local tests, only <---
    And it dome additional logging for demo purpose, only.
    Check the process log when the script executes on the platform.
 */
final Logger _logger = ExecutionUtil.getBaseLogger()
_logger.handlers.each { it.setFormatter(new MyCustomFormatter()) }
_logger.info('>>> Script start i' + SCRIPT_NAME)
_logger.fine('>>> Script start f' + SCRIPT_NAME)
_logger.finer('>>> Script start r' + SCRIPT_NAME)
_logger.finest('>>> Script start st' + SCRIPT_NAME)

/* Platform Log
2024-11-28 11:41:53 AM	INFO	Data Process	[1] Scripting: groovy2	Executing with 100 documents in

2024-11-28 11:41:53 AM	INFO	Data Process	[1] Scripting: groovy2	>>> Script start iSetIDocType
2024-11-28 11:41:53 AM	FINE	Data Process	[1] Scripting: groovy2	>>> Script start fSetIDocType
2024-11-28 11:41:53 AM	FINER	Data Process	[1] Scripting: groovy2	>>> Script start rSetIDocType
2024-11-28 11:41:53 AM	FINEST	Data Process	[1] Scripting: groovy2	>>> Script start stSetIDocType

2024-11-28 11:41:53 AM	FINE	Data Process	[1] Scripting: groovy2	In-Document Count=100
2024-11-28 11:41:53 AM	INFO	Data Process	[1] Scripting: groovy2	Elapsed time: 184 ms
2024-11-28 11:41:53 AM	INFO	Data Process	[1] Scripting: groovy2	Completed with 100 documents out
 */


int docCount = dataContext.getDataCount()
_logger.fine("In-Document Count=" + docCount)

// Stop-Watch
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
_logger.info( "Elapsed time: ${elapsedTime} ms")


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


class MyCustomFormatter extends Formatter {

    static DateTimeFormatter formatter = DateTimeFormatter
            //.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            .ofPattern("HH:mm:ss.SSS")
            .withZone(ZoneId.of("UTC"))

    @Override
    String format(LogRecord record) {
        // threadID is for JVM 11, and deprecated in JVM 2x, user longThreadID instead
        return "${formatter.format(record.instant)}Z | ${record.level}[${record.threadID}] | ${record.message}\n"
    }
}


