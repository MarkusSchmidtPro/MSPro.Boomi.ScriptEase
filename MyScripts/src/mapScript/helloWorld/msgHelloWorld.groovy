package mapScript.helloWorld

import com.boomi.execution.ExecutionUtil

// Give the script a meaningful name
// Start with msg -> Message Script Groovy
final String SCRIPT_NAME = "msgHelloWorld"

/** ================================================================================== 
    The Hello World MapScript logs the current AtomID (taken from the execution context).
    Then it does some math.
 
    INPUT Parameters (DataContext):
        a       : Parameter 1
        b       : Parameter 2

    OUTPUT Parameters
        total   : sum( a, b)
 
    ----------------------------------------------------------------------------------
    2022-07-01  msc -   Created
    ==================================================================================
*/

// Get a logger instance that can be used to log messages 
def _logger = ExecutionUtil.getBaseLogger()
_logger.info('>>> Start Script ' + SCRIPT_NAME)

try {
    /*
     * Get the runtime execution properties and save into local variables.
     * Write information to process reporting (in Boomi) 
     * or to the debug windows when testing.
     */
     String atomId = ExecutionUtil.getRuntimeExecutionProperty("ATOM_ID")
    _logger.info("Atom-ID: $atomId")

    // This is the script's logic - not too much ...
    total = a + b
    
    // Log the result and the end of the execution to process reporting
    _logger.info("$a + $b = $total")
    _logger.info('<<< End Script')
}
catch (Exception e) {
    // In case of a script exception, we log the message and 
    // re-throw the exception to let it pop up in the platform.
    _logger.severe(e.message)
    throw e
}