package processScript.execProps

import groovy.transform.SourceURI
import groovy.transform.TypeChecked
import msPro.scriptease.*
import org.junit.Test

@TypeChecked
class Test_LogExecProperties {
	final String SCRIPT_NAME = "LogExecProperties"

	@SourceURI
	URI _sourceUri
	final ProcessScript _testScript = new ProcessScript("psg" + SCRIPT_NAME + ".groovy", _sourceUri)


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
						Document.fromText('{ "firstname" : "Walter", "lastname" : "Schmidt" }')
				],
				// Override to test process chains 
				processCallChain : [
						new ProcessExecutionProperties( "Custom Main Process"),
						new ProcessExecutionProperties( "sub process 01"),
						new ProcessExecutionProperties( "sub process 01.01")
				])
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
