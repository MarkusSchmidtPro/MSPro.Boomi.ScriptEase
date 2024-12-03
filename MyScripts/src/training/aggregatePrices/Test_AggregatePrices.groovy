package training.aggregatePrices

import groovy.json.JsonOutput
import groovy.transform.SourceURI
import groovy.transform.TypeChecked
import msPro.scriptease.Document
import msPro.scriptease.ProcessScript
import msPro.scriptease.ProcessScriptContext
import msPro.scriptease.TestFilesHelper
import org.junit.Test

@TypeChecked
class Test_AggregatePrices extends GroovyTestCase{
	final String SCRIPT_NAME = "AggregatePrices"

	@SourceURI
	URI _sourceUri
	final ProcessScript _testScript = new ProcessScript("psg" + SCRIPT_NAME + ".groovy", _sourceUri)
	final TestFilesHelper _testFiles = new TestFilesHelper( "testData", _sourceUri)


	/** A short description what this test is supposed to do. */
	@Test
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
						/* Chose the fromText factory to create a Document from a String
						Document.fromText('{ "articleNo" : "a001", "price" : 1.23 }'),
						Document.fromText('{ "articleNo" : "a002", "price" : 2.00 }'),
						Document.fromText('{ "articleNo" : "a001", "price" : 1.02 }'),
						Document.fromText('{ "articleNo" : "a001", "price" : 0.98 }')
						*/
						/* Chose the fromFile factory to load Document data from file.  */
						Document.fromFile( _testFiles.get("rec01.json")),
						Document.fromFile( _testFiles.get("rec02.json")),
						Document.fromFile( _testFiles.get("rec03.json")),
						Document.fromFile( _testFiles.get("rec04.json")),
				])
		_testScript.run(context)


		println("\r\n--- Test Output ----------")

		int docCount = context.outputDocuments.size()
		println(docCount + " Document(s) after script execution")
		assert 1 == docCount

		for (Document doc in context.outputDocuments) {
			String textDoc = doc.toString()
			assert textDoc != "", "Document is null"
			println("Doc:" + JsonOutput.prettyPrint( textDoc))
		}
		
		/* 
		[{
		        "articleNo": "a001",
		        "priceCount": 3,
		        "minPrice": 0.98,
		        "maxPrice": 1.23,
		        "prices": [
		            1.23,
		            1.02,
		            0.98
		        ]
		    },{
		        "articleNo": "a002",
		        "priceCount": 1,
		        "minPrice": 2.00,
		        "maxPrice": 2.00,
		        "prices": [
		            2.00
		        ]
		    }]
		 */
	}
}
