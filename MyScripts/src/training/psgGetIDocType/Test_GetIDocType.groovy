package training.psgGetIDocType


import groovy.transform.SourceURI
import groovy.transform.TypeChecked
import msPro.scriptease.Document
import msPro.scriptease.ProcessScript
import msPro.scriptease.ProcessScriptContext
import msPro.scriptease.TestFilesHelper
import org.junit.Test

@TypeChecked
class Test_GetIDocType {
    final String SCRIPT_NAME = "GetIDocType"

    @SourceURI
    URI _sourceUri
    final ProcessScript _testScript = new ProcessScript("psg" + SCRIPT_NAME + ".groovy", _sourceUri)
    final TestFilesHelper _testFiles = new TestFilesHelper("testData", _sourceUri)

    @Test
    void test03_IDocType() {
        ProcessScriptContext context = new ProcessScriptContext(
                inputDocuments: [
                        Document.fromFile(_testFiles.get("Export_WMMBID02.xml")),
                        Document.fromFile(_testFiles.get("Export_ZTOUR02.xml")),
                        Document.fromFile(_testFiles.get("Export_ZTOURAUF.xml"))
                ])
        _testScript.run(context)

        println("\r\n--- Test Output ----------")

        int docCount = context.outputDocuments.size()
        println(docCount + " Document(s) after script execution")
        assert context.inputDocuments.size() == docCount,
                "Document count mismatch! Expected: all documents which were passed into the script."


        for (Document doc in context.outputDocuments) {
            String textDoc = doc.toString()
            assert textDoc != "", "Document must not be empty!"

            // Default value: null
            String errorMessage = getPropertyOrDefault(doc, "DDP_Error")
            // Default value: ""
            String idocType = getPropertyOrDefault( doc, "DDP_IDocType", {return ""})

            if (errorMessage == null || errorMessage.isEmpty()) {
                assert !idocType.isEmpty(), "DDP_IDocType must be empty in case of success!"
                println("Known IDoc-Type='$idocType'")
            } else {
                assert idocType.isEmpty(), "DDP_IDocType should be empty in case of an error!"
                println("UNKNOWN IDoc-Type: $errorMessage")
            }
        }
    }

//
// Property helpers
//
    private static String getPropertyOrDefault(Document doc, String propertyName, Closure<String> unknownProperty = null) {
        final String userDefinedPropertyBase = 'document.dynamic.userdefined.'

        if (doc.getProperties().containsKey(userDefinedPropertyBase + propertyName))
            return doc.getProperty(propertyName)

        // Property does not exist
        // call-back the closure or return null
       return unknownProperty != null ? unknownProperty(propertyName) : null
    }
}