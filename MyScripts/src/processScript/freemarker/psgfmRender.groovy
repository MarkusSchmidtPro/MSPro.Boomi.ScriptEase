package processScript.freemarker

import com.boomi.execution.ExecutionUtil
import freemarker.template.Configuration
import freemarker.template.Template
import groovy.json.JsonSlurper

final String SCRIPT_NAME = "fmRender"

/* **************************************************************************
    Render a template with freemarker
        
    IN : [Describe inbound arguments]
    OUT: [Describe outbound arguments]
    ------------------------------------------------
    2026-02-14  MSPRO -   Created
    Template v0.2.2
************************************************************************** */

// fine=DEBUG, info=INFO, warning=WARNING, severe=SEVERE
final _logger = ExecutionUtil.getBaseLogger()
_logger.fine('>>> Script start ' + SCRIPT_NAME)

try {
    int docCount = dataContext.getDataCount()
    _logger.fine("In-Document Count=" + docCount)

//    def templateSource = """
//        Project: \${project}
//        Version: \${version}
//        Keywords:
//        <#list tags as t>
//          - \${t}
//        </#list>
//        """
    // Configure FreeMarker
    // In Groovy 2.4/older environments, use the Version constructor directly
    def cfg = new Configuration(Configuration.VERSION_2_3_23)
    cfg.setInterpolationSyntax(Configuration.DOLLAR_INTERPOLATION_SYNTAX)
    
    String templateSource = _getDPP("DPP_Template")
    def reader = new StringReader(templateSource)
    def template = new Template("strTemplate", reader, cfg)

    
    for (int docNo = 0; docNo < docCount; docNo++) {
        final String textDoc = _getTextDocument(docNo)
        final Properties props = dataContext.getProperties(docNo)

        // *********** Document related functionality ************

        //def textDoc = '{"project": "Apollo", "version": "2.4", "tags": ["Groovy", "FreeMarker"]}'
        def dataModel = new JsonSlurper().parseText(textDoc)

        // 4. Render
        def writer = new StringWriter()
        template.process(dataModel, writer)
        
        // ******** end of Document related functionality ********

        _setTextDocument(writer.toString(), props)
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

// region Document as Text

/** Get a a document from the [dataContext] (input stream).
 * @param docNo Document number : 0 ... dataContext.getDataCount() -1
 * @return The document as a String, UTF-8 decoded from the input stream.
 */
private String _getTextDocument(int docNo) {
    InputStream documentStream = dataContext.getStream(docNo)
    return documentStream.getText("UTF-8")
}


/** Write a (text) document back into the output stream.
 * @param value The document text as String.
 * @param props The document's dynamic properties as the have been read
 *                using `final Properties props = dataContext.getProperties(docNo)`.
 */
private void _setTextDocument(String value, Properties props) {
    InputStream newDocumentStream = new ByteArrayInputStream(value.getBytes("UTF-8"))
    dataContext.storeStream(newDocumentStream, props)
}

/** Get a dynamic process property.
 *
 * @param propertyName
 * @param default If default is null, an exception is thrown when the property is not set.
 *                      Otherwise, the default is returned in case it does not exist (is empty).
 *                      Whitespaces are valid characters and a whitespaces string is not empty!
 * @return The property value (or default)
 */
private static String _getDPP(String propertyName, String defaultValue = null) {
    String v = ExecutionUtil.getDynamicProcessProperty(propertyName)
    if (v == null || v.isEmpty()) {
        // Default handler
        if (defaultValue == null) throw new Exception("Mandatory " + propertyName + " not set.")
        v = defaultValue
    }
    return v
}
// endregion

