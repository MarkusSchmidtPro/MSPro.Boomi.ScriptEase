package processScript.freemarker

import groovy.transform.*
import msPro.scriptease.*
import org.junit.Test

@TypeChecked
class Test_fmRender extends GroovyTestCase {
    final String SCRIPT_NAME = "fmRender"

    @SourceURI
    URI _sourceUri
    final ProcessScript _testScript = new ProcessScript("psg" + SCRIPT_NAME + ".groovy", _sourceUri)
    final TestFilesHelper _testFiles = new TestFilesHelper("testData", _sourceUri)

    /** A short description what this test is supposed to do. */
    @Test
    void test01() {
        // Initialize the Script Execution Context:
        // * Execution Properties       : executionProperties
        // * Dynamic Process Properties : dynProcProps
        // * Process Properties         : procPros
        // * Documents                  : inputDocuments
        //      incl. Dynamic Document Properties
        // --------------------------------------------------------------

        ProcessScriptContext context = new ProcessScriptContext(
                inputDocuments: [
                        Document.fromFile( _testFiles.get( "certificate-data.json"))
                ],
                dynProcProps: [
                        DPP_Template: _testFiles.get( "certificate-template.html").readLines(),
                ])
        _testScript.run(context)

        println("\r\n--- Test Output ----------")

        int docCount = context.outputDocuments.size()
        println(docCount + " Document(s) after script execution")
        assert context.inputDocuments.size() == docCount

        for (Document doc in context.outputDocuments) {
            String textDoc = doc.toString()
            assert textDoc != "", "Document is null"
            println("Doc:" + textDoc)
        }
    }
}
