package training.xmlToJson

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.transform.*
import msPro.scriptease.*
import org.junit.Test

@TypeChecked
class Test_xmlToJson extends GroovyTestCase {
    final String SCRIPT_NAME = "xmlToJson"

    @SourceURI
    URI _sourceUri
    final ProcessScript _testScript = new ProcessScript("psg" + SCRIPT_NAME + ".groovy", _sourceUri)
    final TestFilesHelper _testFiles = new TestFilesHelper("testData", _sourceUri)

    /** A short description what this test is supposed to do. */
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
                        Document.fromFile( _testFiles.get( "doc01.XML")),
                        Document.fromFile( _testFiles.get( "doc02.XML"))
                ],
                dynProcPros: [
                        DPP_Prop01: "2024",
                ])
        _testScript.run(context)

        println("\r\n--- Test Output ----------")

        int docCount = context.outputDocuments.size()
        println(docCount + " Document(s) after script execution")
        assert context.inputDocuments.size() == docCount

        for (Document doc in context.outputDocuments) {
            String textDoc = doc.toString()
            assert textDoc != "", "Document is null"
            println("Doc:" + prettyJson(textDoc))
        }
    }


	static String prettyJson(String s){
		// call overload
		return prettyJson( new JsonSlurper().parseText(s))
	}

	// Overloaded method with different parameters.
	static String prettyJson( Object jObject){
		def jString = JsonOutput.toJson(jObject)
		return JsonOutput.prettyPrint( jString)
	}
}
