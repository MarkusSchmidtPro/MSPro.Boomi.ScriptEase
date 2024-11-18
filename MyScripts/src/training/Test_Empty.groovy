package training

import groovy.transform.*
import org.junit.Test

@TypeChecked
class Test_Empty {
    final String SCRIPT_NAME = "Empty"

    @SourceURI
    URI _sourceUri
    final ProcessScript _testScript = new ProcessScript("psg" + SCRIPT_NAME + ".groovy", _sourceUri)
    final TestFilesHelper _testFiles = new TestFilesHelper("testData", _sourceUri)

    /** A short description what this test is supposed to do. */
    @Test
    void test01() {
        // Initialize the Script Execution Context:
        // * Execution Properties       : executionProperties
        // * Dynamic Process Properties : dynProcPros
        // * Process Properties         : procPros
        // * Documents                  : inputDocuments
        //      incl. Dynamic Document Properties
        // --------------------------------------------------------------

        ProcessScriptContext context = new ProcessScriptContext(
                inputDocuments: [
                        Document.fromText('{ "firstname" : "Walter", "lastname" : "Schmidt" }')
                        //, Document.fromFile( _testFiles.get( "filename.XML"))
                ],
                dynProcPros: [
                        DPP_Prop01: "2024",
                ] as HashMap)
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
