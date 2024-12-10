package processScript.businessRuleResultToString


import groovy.transform.SourceURI
import groovy.transform.TypeChecked
import msPro.scriptease.Document
import msPro.scriptease.ProcessScript
import msPro.scriptease.ProcessScriptContext
import msPro.scriptease.TestFilesHelper
import org.junit.Test

@TypeChecked
class Test_BRResultToString extends GroovyTestCase {
    final String SCRIPT_NAME = "BRResultToString"

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
                /*
                    Initialize two (dummy) documents, 
                    with each having a DDP_business_rule_result
                    that is read from a file in ".\testData".  
                 */
                inputDocuments: [
                        Document.fromText('empty01 - doc content is not of interest',
                                [DDP_BusinessRuleResult: _testFiles.getText("result01.xml")]),
                        Document.fromText('22012013174480;Pip Service GmbH & Co. KG;Munich ...',
                                [DDP_BusinessRuleResult: _testFiles.getText("result02.xml")])
                ])
        _testScript.run(context)

        println("\r\n--- Test Output ----------")

        int docCount = context.outputDocuments.size()
        println(docCount + " Document(s) after script execution")
        assert context.inputDocuments.size() == docCount

        // Define the number of error messages 
        // which are expected for each document.
        int[] expectedErrorsCount = [1, 2]
        final String MESSAGE_SEPARATOR = "\r\n"

        int docNo = 0
        for (Document doc in context.outputDocuments) {
            String textDoc = doc.toString()
            assert textDoc != "", "Document is null"

            //
            // Get the result message from the dynamic document property
            // and check it contains the right number of messages.
            //
            String resultMessage = doc.getProperty("DDP_BusinessRuleResult")
            // Split into n lines
            String[] resultMessages = resultMessage.split(MESSAGE_SEPARATOR)

            // Print each line
            //for( int lineNo = 0; lineNo< resultMessages.size(); lineNo++)
            println("Error[$docNo]")
            resultMessages.each({ String msg ->
                // https://chatgpt.com/share/6757f08e-e448-8002-81f6-0e229fcaf6ed
                println(msg.padLeft(2 + msg.size(), "-"))
            })
            println("----------")

            assert expectedErrorsCount[docNo] == resultMessages.size()
            docNo++;
        }
    }
}
