package processScript.aggregatePrices

import groovy.json.JsonOutput
import groovy.transform.SourceURI
import groovy.transform.TypeChecked
import msPro.scriptease.Document
import msPro.scriptease.ProcessScript
import msPro.scriptease.ProcessScriptContext
import org.junit.Test

@TypeChecked
class Test_AggregatePrices {
	final String SCRIPT_NAME = "AggregatePrices"

	@SourceURI
	URI _sourceUri
	final ProcessScript _testScript = new ProcessScript("psg" + SCRIPT_NAME + ".groovy", _sourceUri)


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
						Document.fromText('{ "articleNo" : "a001", "price" : 1.23 }'),
						Document.fromText('{ "articleNo" : "a002", "price" : 2.00 }'),
						Document.fromText('{ "articleNo" : "a001", "price" : 1.02 }'),
						Document.fromText('{ "articleNo" : "a001", "price" : 0.98 }')
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
