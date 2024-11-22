package processScript.executionProperties

import com.boomi.execution.ExecutionManager
import com.boomi.execution.ExecutionTask
import com.boomi.execution.ExecutionUtil

import java.util.logging.Logger

final String SCRIPT_NAME = "LogExecProperties"

/* **************************************************************************
    Log all execution properties
    ------------------------------------------------
    2024-06-26  mspro   - created
    Template v0.2.1
************************************************************************** */


_logger = ExecutionUtil.getBaseLogger()
_logger.info('>>> Script start ' + SCRIPT_NAME)
try {
	_logExecProperties(_logger)

	def currentProcessExecution = ExecutionManager.getCurrent()
	
	_logExecTaskProperties(currentProcessExecution)

	// A bit of business logic to retrieve the top-level process name
	// when the script was started from a sub-process (or process route).

	// Iterate up to top-level though executionTasks using getParent()

	_logger.info( "*** Script hosting process name  : " +  currentProcessExecution.getProcessName())
	ExecutionTask t = currentProcessExecution
	while( t.getParent() != null) t = t.getParent()
	_logger.info( "*** Top-Process Name             : " +  t.getProcessName())
	// *** Script hosting process name  : sub process 01.01
	// *** Top-Process Name             : Custom Main Process
	
	// ------------ Document pass though --------------------- 
	int docCount = dataContext.getDataCount()
	//_logger.fine("In-Document Count=" + docCount)
	for (int docNo = 0; docNo < docCount; docNo++) {
		final String textDoc = _getTextDocument(dataContext, docNo)
		final Properties props = dataContext.getProperties(docNo)
		// *********** Document related functionality ************
		// Your document related code here ...
		// ******** end of Document related functionality ********
		_setTextDocument(dataContext, textDoc, props)
	}
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


/**
 * Execution Properties are related to {@link ExecutionUtil} and fix per execution
 */
private static void _logExecProperties(Logger _logger) {
	final String[] wellKnownExecProps = [
			"ACCOUNT_ID", "ATOM_ID", "ATOM_NAME", "NODE_ID",
			"DOCUMENT_COUNT",
			"EXECUTION_ID", "PROCESS_ID", "PROCESS_NAME"
	]

	_logger.info("+ ExecutionUtil Properties")
	for (String p in wellKnownExecProps) {
		_logger.info("- $p = " + ExecutionUtil.getRuntimeExecutionProperty(p))
	}
	_logger.info("- ContainerId = " + ExecutionUtil.getContainerId())
}

/**
 * Execution Task Properties are related to {@link ExecutionTask}.
 * There is an ExecutionTask instance for each process. The script's hosting process
 * is the current (and deepest) one in the _ScriptContext.processCallChain_ (level=0).
 * You can traverse up in the call hierarchy by providing a higher level.
 * The highest level is called: top-level.
 */
void _logExecTaskProperties(ExecutionTask t, int level = 0) {
	ExecutionTask p = t.getParent()
	if (p != null) _logExecTaskProperties(p, level + 1)

	_logger.info("------------------------------------------------------------------------------")
	_logger.info("+ ExecutionTask Properties - Level=" + level + " - bottom (=0) up to top-level")
	_logger.info("- Is Top-Level           = " + (p == null).toString())
	_logger.info("- Id                     = " + t.getId())
	_logger.info("- Execution Id           = " + t.getExecutionId())
	_logger.info("- Process   Name         = " + t.getProcessName())
	_logger.info("- Process   Id           = " + t.getProcessId())
	_logger.info("- Component Id           = " + t.getComponentId())
	_logger.info("- Start Time             = " + t.getStartTime())

	if (level == 0) {
		// Print only on current level	
		_logger.info("---- static ---- ")
		_logger.info("- Top-Level Execution Id = " + t.getTopLevelExecutionId())
		_logger.info("- Top-Level Process   Id = " + t.getTopLevelProcessId())
		_logger.info("- Top-Level Component Id = " + t.getTopLevelComponentId())
		_logger.info("- Account Id             = " + t.getAccountId())
	}
}