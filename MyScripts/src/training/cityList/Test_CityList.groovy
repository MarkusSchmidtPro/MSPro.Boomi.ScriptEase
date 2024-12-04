package training.cityList

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.transform.*
import msPro.scriptease.*

@TypeChecked
class Test_CityList extends GroovyTestCase {
    final String SCRIPT_NAME = "CityList"

    @SourceURI
    URI _sourceUri
    final ProcessScript _testScript = new ProcessScript("psg" + SCRIPT_NAME + ".groovy", _sourceUri)
    final TestFilesHelper _testFiles = new TestFilesHelper("testData", _sourceUri)

    /** A short description what this test is supposed to do. */
    void test01() {
        // Initialize the Script Execution Context:
        // * Execution Properties       : execProps
        // * Dynamic Process Properties : dynProcProps
        // * Process Properties         : procProps
        // * Documents                  : inputDocuments
        //      incl. Dynamic Document Properties
        // --------------------------------------------------------------

        ProcessScriptContext context = new ProcessScriptContext(
                inputDocuments: [
                        Document.fromTextFile(_testFiles.get("rec01.json")),
                        Document.fromTextFile(_testFiles.get("rec02.json")),
                        Document.fromTextFile(_testFiles.get("rec03.json")),
                        Document.fromTextFile(_testFiles.get("rec04.json"))
                ])
        _testScript.run(context)

        println("\r\n--- Test Output ----------")

        int docCount = context.outputDocuments.size()
        println(docCount + " Document(s) after script execution")
        assert 1 == docCount

        for (Document doc in context.outputDocuments) {
            String textDoc = doc.toString()
            assert textDoc != "", "Document is null"
            
            // Validate Document
            
            int cityCount = doc.getProperty("DDP_CityCount") as int
            assert cityCount == 3

            Map jObject = new JsonSlurper().parseText(textDoc) as Map
            
            /*
            def citizensOfAachen = jObject["Aachen"]
            assert citizensOfAachen instanceof ArrayList, "A city should contain an ArrayList of citizens!"
            assert citizensOfAachen.size() == 1, "Based on the test data, Aachen should have one citizen!"

            assert (citizensOfAachen.first instanceof Map), "Based on the test data, Aachen's first citizen should be of type Map!"
            // If you want to access the ArrayList by index,
            // you must let the compiler explicitly know, 
            // that citizensOfAachen is an ArrayListÖ as keyword.
            // assert (citizensOfAachen as ArrayList)[0] instanceof Map, "Based on the test data, Aachen's first citizen should be of type Map!"
            
            Map firstCitizenOfAachen = citizensOfAachen.first as Map
            assert firstCitizenOfAachen.street == null, "Based on the test data, Aachen's first citizen should not have a street!"
            assert firstCitizenOfAachen.name != null, "Based on the test data, Aachen's first citizen should have a name!"
            
             */
            println("Doc:" + prettyJson(textDoc))
            
            // compare the results as Json, not text: a blank could make the difference!
            String resultText = _testFiles.get("rec_010203_result.json").getText()
            def jResult = new JsonSlurper().parseText(resultText)
            assert jResult == jObject
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
