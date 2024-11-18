package processScript.tests

import groovy.json.JsonOutput
import groovy.transform.SourceURI
import groovy.transform.TypeChecked
import msPro.scriptease.Document
import msPro.scriptease.ProcessScript
import msPro.scriptease.ProcessScriptContext
import msPro.scriptease.TestFilesHelper
import org.junit.Test

/**
 * This test class contains several test that show you how to 
 * inject Process Properties and Dynamic Process Properties into a process script.
 */
@TypeChecked
class Test_ProcessProperties {
	// region Header
	final String SCRIPT_NAME = "Null"

	@SourceURI
	URI _sourceUri
	final ProcessScript _testScript = new ProcessScript("psg" + SCRIPT_NAME + ".groovy", _sourceUri)
	// endregion

	/** A showcase how to create multiple documents inline, from code.
	 *
	 * The document content is a Json profile. 
	 * We use the {@code Document.fromText()} factory method in different ways 
	 * to inject JSON documents.
	 */
	@Test
	void test_dynamicDocumentProperties() {

		ProcessScriptContext context = new ProcessScriptContext(
				inputDocuments: [
						Document.fromText('{ "firstname" : "Walter", "lastname" : "Schmidt" }')
				],
				dynProcPros: [
						"DPP_DynProcProp01": 1,
						"DPP_DynProcProp02": "Value01"
				] as HashMap
		)
		_testScript.run(context)
		_onAfterScript(context)
	}

	/** Create process properties.
	 */
	@Test
	void test_ProcessProperties() {
		final String ppMessageContext = "b91d87a4-7e8b-4a98-8ea8-a85e32bb5677"
		final String ppKeyServiceIncident = "fcc4749d-5135-4eaa-a9cf-1b2ddc1ad12"
		final String ppKeySenderAddress = "anotherGuid"

		// Wrap keys in parenthesis 
		// so that the variables (Ids) are taken and not the text as a string

		ProcessScriptContext context = new ProcessScriptContext(
				inputDocuments: [Document.fromText('{ "firstname" : "Walter", "lastname" : "Schmidt" }')],
				procPros: 
						[
							(ppMessageContext): [
									(ppKeyServiceIncident): "Incident_01",
									(ppKeySenderAddress)  : "mailto@google.com"
							]
						])

		_testScript.run(context)
		_onAfterScript(context)
	}


	private static void _onAfterScript(ProcessScriptContext context) {
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
