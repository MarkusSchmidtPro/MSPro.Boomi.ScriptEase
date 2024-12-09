package training.aggregatePrices

import groovy.json.JsonOutput
import groovy.transform.SourceURI
import groovy.transform.TypeChecked
import msPro.scriptease.Document
import msPro.scriptease.ProcessScript
import msPro.scriptease.ProcessScriptContext
import msPro.scriptease.TestFilesHelper
import org.junit.Test

class Test_AggregatePricesX extends GroovyTestCase{
	final String SCRIPT_NAME = "AggregatePricesX"

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
						/* Chose the fromFile factory to load Document data from file.  */
						Document.fromFile( _testFiles.get("rec01.xml")),
						Document.fromFile( _testFiles.get("rec02.xml")),
						Document.fromFile( _testFiles.get("rec03.xml")),
						Document.fromFile( _testFiles.get("rec04.xml")),
				])
		_testScript.run(context)


		println("\r\n--- Test Output ----------")

		int docCount = context.outputDocuments.size()
		println(docCount + " Document(s) after script execution")
		assert 1 == docCount

		for (Document doc in context.outputDocuments) {
			String textDoc = doc.toString()
			assert textDoc != "", "Document is null"
			println("Doc:" + textDoc)
			
			// Use XmlParser - not Slurper !!!
			def root = new XmlParser().parseText(textDoc)
			assert root.Article.size() == 2
		}
	}
}
