package mapScript.dateTime

import com.boomi.execution.ExecutionUtil

final String SCRIPT_NAME = "msgDateTimeParse"

/** ==================================================================================
 Parse a datetime string.

 IN : [Describe inbound variables]
 dateTimeString       : A String containing a DateTime
 inputFormat          : The expected format for [dateTimeString]

 OUT:
 dateTimeFormatted : The DateTime formatted in the
 Boomi Standard format: "yyyyMMdd HHmmss.SSS"

 ------------------------------------------------
 04.11.2025  mspro -   Created
 ==================================================================================
 */

final _logger = ExecutionUtil.getBaseLogger()
_logger.info('>>> Start Script ' + SCRIPT_NAME)
_logger.info("IN ='$dateTimeString'")
_logger.info("FMT='$inputFormat'")

try {
    // ------------------------
    // Your logic goes here ..
    def date = Date.parse(inputFormat, dateTimeString)
    if( (outputFormat as String).length() ==0) outputFormat = "yyyyMMdd HHmmss.SSS"
    dateTimeFormatted = date.format(outputFormat)

    // ----- end of logic -----
}
catch (Exception e) {
    _logger.severe(e.message)
    throw e
}
_logger.info('<<< End Script')