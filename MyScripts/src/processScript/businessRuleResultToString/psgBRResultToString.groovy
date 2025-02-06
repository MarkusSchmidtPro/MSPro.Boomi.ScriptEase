package processScript.businessRuleResultToString
import com.boomi.execution.ExecutionUtil

final String SCRIPT_NAME = "BRResultToString"

/* **************************************************************************
    Convert a BusinessRule error message (XML) into a user readable string.
    
    IN:     DDP_BusinessRuleResult (XML)
    
    OUT:    DDP_BusinessRuleResult (string)
    
    ------------------------------------------------
    2024-07-24  msc -   Back to DDP_ 
                    -   DDP renamed to camel-case: DDP_BusinessRuleResult
    2024-05-21  msc -   DDP_ to D_
                    -   MESSAGE_SEPARATOR introduced 
    17.04.2024  msc -   Created
************************************************************************** */
final String DDP_PROP_NAME = "DDP_BusinessRuleResult"


// region IMPORTANT NOTE 
// "region" makes this region collapsible!
// =========================================================================
// IMPORTANT: It is worst practice to use Regex to "parse" XML as text.
// However, some people might think: it is faster to use regex. Maybe, it is, 
// and if it takes double the time, your script will run for 10ms instead of 5ms. So what?
// ==> Use XmlSlurper or XmlParser!
//
// Pattern to extract a business rule failure from any text.
// final String brPattern = "\\<business_rule_failures\\>.*\\<\\/business_rule_failures\\>"
//      final Matcher brXml = childPattern.matcher( ddpErrorMessage)
//      if( brXml.find()){
//          ddpErrorMessage = _businessRuleToString(brXml.group(0) )
// =========================================================================
// endregion

final _logger = ExecutionUtil.getBaseLogger()
_logger.info('>>> Script start ' + SCRIPT_NAME)

try {
    _logger.fine("In-Document Count= " + dataContext.getDataCount())

    for (int docNo = 0; docNo < dataContext.getDataCount(); docNo++) {
        
        String document = _getTextDocument(dataContext, docNo)
        Properties props = dataContext.getProperties(docNo)

        // *********** Script functionality ************

        String ddpErrorMessage = _getDDP( props, DDP_PROP_NAME)
        ddpErrorMessage = _businessRuleToString( ddpErrorMessage )
        _setDDP( props, DDP_PROP_NAME, ddpErrorMessage)
        _logger.fine("${DDP_PROP_NAME}='${ddpErrorMessage}'")

        // ******** end of Script functionality ********

        _setTextDocument(dataContext, document, props)
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

static String _getDDP(Properties docProperties, String propertyName, String defaultValue = null) {
    final String userDefinedPropertyBase = 'document.dynamic.userdefined.'
    String v = docProperties.getProperty(userDefinedPropertyBase + propertyName)
    if (v == null || v.isEmpty()) {
        // Default handler
        if (defaultValue == null) throw new Exception("Mandatory " + propertyName + " not set.")
        v = defaultValue
    }
    return v
}

static String _setDDP(Properties docProperties, String propertyName, String value) {
    final String userDefinedPropertyBase = 'document.dynamic.userdefined.'
    docProperties.setProperty(userDefinedPropertyBase + propertyName, value)
}

private static String _businessRuleToString(String businessRuleXml) {
    final String MESSAGE_SEPARATOR = "\r\n"

    /*  businessRuleXml=
         <business_rule_failures>
             <business_rule_failure rule="Validate PLZ">null ist nicht befüllt</business_rule_failure>
         </business_rule_failures>
    */
    def business_rule_failures = new XmlSlurper().parseText(  businessRuleXml)
    List<String> messages =[]
    for( def nBusiness_rule_failure in business_rule_failures.business_rule_failure){
        String rule = nBusiness_rule_failure.@'rule'
        String message = nBusiness_rule_failure
        messages.add( "${rule}: ${message}")
    }

    return messages.join( MESSAGE_SEPARATOR)
}
