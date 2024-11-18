package processScript.tests

import groovy.json.JsonOutput
import groovy.transform.SourceURI
import groovy.transform.TypeChecked
import msPro.scriptease.*
import org.junit.Test

/**
 * This test class contains several test that show you how to 
 * inject Documents with and without Dynamic Document Properties 
 * into a process script.
 */
@TypeChecked
class Test_DocumentsAndDDP {
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
	void test_documentsFromText() {

		ProcessScriptContext context = new ProcessScriptContext(
				inputDocuments: [
						Document.fromText('{ "firstname" : "Walter", "lastname" : "Schmidt" }'),
						Document.fromText( JsonOutput.toJson( [ firstname : "John", lastname : "Doe" ])),
						Document.fromText('''
						{ 
							"firstname" : "Walter Jr.", 
							"lastname" : "Miller" 
						}
						'''), // Groovy multi-line text support
				])
		_testScript.run(context)
		_onAfterScript(context)
	}

	/** Add document from files.
	 */
	@Test
	void test_documentsFromFiles() {

		final TestFilesHelper _testFiles = new TestFilesHelper( "testData", _sourceUri)

		ProcessScriptContext context = new ProcessScriptContext(
				inputDocuments: [
						Document.fromFile( _testFiles.get( "doc01.json")),
						Document.fromFile( _testFiles.get("doc02.json"))
				])
		_testScript.run(context)
		_onAfterScript(context)
	}

	/** Demos how to provide Dynamic Document Properties
	 */
	@Test
	void test_documentsWithProperties() {

		// Add three documents with DDP_Prop1 and DDP_Prop02 each
		ProcessScriptContext context = new ProcessScriptContext(
				inputDocuments: [
						Document.fromText('Doc Content01', 
						[
						        DDP_Prop01: "Doc1_Value1", DDP_Prop02: "Doc1_P2"
						]),
						Document.fromText('Doc Content02', 
						[
						        DD_Prop01: "Doc2_Value1", DDP_Prop02: "Doc2_P2"
						]),
						Document.fromText('Doc Content03', 
						[
						        DPP_Prop01: "Doc3_Value1", DPP_Prop02: "Doc3_P2"
						])
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
