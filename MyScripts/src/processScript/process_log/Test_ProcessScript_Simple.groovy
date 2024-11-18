package processScript.process_log


import groovy.transform.SourceURI
import groovy.transform.TypeChecked
import msPro.scriptease.Document
import msPro.scriptease.ProcessScript
import msPro.scriptease.ProcessScriptContext
import org.junit.Test

/**
 * Demo MGF4Boomi functionality.
 */
@TypeChecked
class Test_ProcessScript_Simple {

	@SourceURI
	URI _sourceUri
	final ProcessScript _testScript = new ProcessScript("psgTrace.groovy", _sourceUri)


	/**
	 * Run script with empty DataContext => no documents!
	 */
	@Test
	void test01() {
		def context = new ProcessScriptContext()
		_testScript.run(context)

		println("\r\n--- Test Output ----------")
		printDocuments(context.outputDocuments)
	}


	/**
	 * Run script with two Strings as documents!
	 */
	@Test
	void test02() {
		ProcessScriptContext context = new ProcessScriptContext(
				inputDocuments: [
						Document.fromText("Document A"),
						Document.fromText("Document B"),
				])

		_testScript.run(context)

		println("\r\n--- Test Output ----------")
		printDocuments(context.outputDocuments)
	}

	private static void printDocuments(Iterable<Document> documents) {
		int docNo = 0
		for (Document doc in documents) {
			println("Doc[${docNo++}]=" + doc.toString())
		}
	}
}
