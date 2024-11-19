
import com.boomi.execution.ExecutionUtil

final String SCRIPT_NAME = "${Plain_BoomiScriptName}"

/** ================================================================================== 
    ${SingleLineDescription}
        
    IN : [Describe inbound variables]
        a       : Parameter 1
        b       : Parameter 2
        
    OUT: [Describe outbound variables]
    ------------------------------------------------
    ${DATE}  ${Author} -   Created
    Template v0.1.0
    ==================================================================================
*/

final _logger = ExecutionUtil.getBaseLogger()
_logger.info('>>> Start Script ' + SCRIPT_NAME)
try {
    // ------------------------
    // Your logic goes here ..
    total = a + b
    // ----- end of logic -----
}
catch (Exception e) {
    _logger.severe(e.message)
    throw e
}
_logger.info('<<< End Script')