package processScript.tests

import com.boomi.execution.ExecutionUtil
import groovy.json.JsonSlurper

final String SCRIPT_NAME = "Null"

/* **************************************************************************
	This script does not implement any functionality.
	It is there to take anything that was injected by the Test - any context.
	Set a breakpoint and see what you get.
    ------------------------------------------------
    14.06.2024   - mspro    Created
    Template v0.2.1
************************************************************************** */

def js = new JsonSlurper()

final _logger = ExecutionUtil.getBaseLogger()
_logger.info('>>> Script start ' + SCRIPT_NAME)
try {
	int docCount = dataContext.getDataCount()
	_logger.fine("In-Document Count=" + docCount)

	for (int docNo = 0; docNo < docCount; docNo++) {
		final String textDoc = _getTextDocument(dataContext, docNo)
		final Properties props = dataContext.getProperties(docNo)

		// *********** Document related functionality ************
		// Your document related code here ...
		_logger.info( "Doc[$docNo]: " + textDoc)
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

static String _getTextDocument(dataContext, int docNo) {
	InputStream documentStream = dataContext.getStream(docNo)
	return documentStream.getText("UTF-8")
}

static void _setTextDocument(dataContext, String value, Properties props) {
	InputStream newDocumentStream = new ByteArrayInputStream(value.getBytes("UTF-8"))
	dataContext.storeStream(newDocumentStream, props)
}
