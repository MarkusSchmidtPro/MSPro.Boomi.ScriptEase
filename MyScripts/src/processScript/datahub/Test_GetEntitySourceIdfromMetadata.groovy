package processScript.datahub

import groovy.transform.*
import msPro.scriptease.*
import org.junit.Test

@TypeChecked
class Test_GetEntitySourceIdfromMetadata extends GroovyTestCase {
    final String SCRIPT_NAME = "GetEntitySourceIdfromMetadata"

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
                        Document.fromText('''
                            <Record recordId="59c7cd73-0d25-4a0a-a86a-9b69f7745b86">
                              <links>
                                <link source="NAVBC" entityId="NAV_0001" establishedDate="2026-02-16T09:35:50Z"/>
                                <link source="EFX" entityId="EFX-1" establishedDate="2026-02-17T08:43:44Z"/>
                              </links>
                            </Record>
                            ''' )
                ], dynProcProps: [DPP_SourceId: "EFX"])
        _testScript.run(context)

        println("\r\n--- Test Output ----------")

        int docCount = context.outputDocuments.size()
        println(docCount + " Document(s) after script execution")
        assert context.inputDocuments.size() == docCount

        for (Document doc in context.outputDocuments) {
            String textDoc = doc.toString()
            assert textDoc != "", "Document is null"
            //println("Doc:" + textDoc)
        }
    }
}
