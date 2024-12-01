package processScript.demo

import groovy.json.JsonSlurper
import groovy.transform.SourceURI
import groovy.transform.TypeChecked
import msPro.scriptease.*
import org.junit.Test

@TypeChecked
class Test_DemoAll {

	@SourceURI
	URI _sourceUri
	final ProcessScript _testScript = new ProcessScript("psgDemoAll.groovy", _sourceUri)
	
	final TestFilesHelper _testFiles = new TestFilesHelper( "testData", _sourceUri)


	/**
	 * Test 
	 */
	@Test
	void test01() {

		ProcessScriptContext context = new ProcessScriptContext()
		// Initialize 
		// * Execution context          : executionContexts
		// * Dynamic Process Properties : dynProcPros
		// * Process Properties         : procPros
		// * Documents                  : inputDocuments
		//      incl. Dynamic Document Properties
		// --------------------------------------------------------------

		// context.executionProperties.ACCOUNT_ID = "IntelliJ_IDEA-M42S66"

		context.dynProcPros.DPP_ProcPropString = "My Process Property"
		context.dynProcPros.DPP_IntValue = 0

		// region Process Property 

		final String PROCESS_PROPERTY_COMPONENT_ID = "8fb41f63-a988-4778-8cc8-0144f30ace81"
		final String VAL1_ID = "eea9e988-cb14-4a84-ba37-ee455451a741"
		final String VAL2_ID = "2c68fb60-8431-46cc-9da9-cbe10d446a0e"

		// Wrap key in parenthesis so that the variables (Ids) are taken
		// and not the text as a string

		def procPropValue1 = 4711
		def procPropValue2 = "Markus Schmidt"
		
		context.procPros = [(PROCESS_PROPERTY_COMPONENT_ID): [
				(VAL1_ID): procPropValue1,
				(VAL2_ID): procPropValue2
		]]
		// endregion

		// region Documents

		final String DDP_Name = "DDP_IntValue"
		int[] ddpValues = [ 10, 11, 12]
		
		context.inputDocuments = [
				Document.fromText('''
				{
					"firstname" : "Walter",
					"lastname" : "Schmidt"
				}''', [(DDP_Name): ddpValues[0]]),
				Document.fromFile( _testFiles.get( "demoDoc01.json") , [(DDP_Name): ddpValues[1]]),
				Document.fromFile( _testFiles.get( "demoDoc02.json") , [(DDP_Name): ddpValues[2]])
		]

		// endregion

		_testScript.run(context)

		//
		// Check and print test result for each document
		//
		println("Script Test Output")
		println("------------------")
		
		// Validate our expectations
		
		int documentCount = context.outputDocuments.size()
		println("${context.outputDocuments.size()} Document(s) after script execution")
		assert documentCount == context.outputDocuments.size() 

		assert context.dynProcPros.DPP_IntValue == "1"

		def pp = context.procPros[PROCESS_PROPERTY_COMPONENT_ID]
		assert pp[ VAL1_ID] == procPropValue1 + 1
		assert pp[ VAL2_ID] == procPropValue2

		def js = new JsonSlurper()
		for (int docNo = 0; docNo < documentCount; docNo++) {
			Document doc = context.outputDocuments[docNo]
			String textDoc = doc.toString()
			assert textDoc != "", "Document is null"

			Map jsonDoc = js.parseText( textDoc) as Map
			String fullName = "${jsonDoc.lastname}, ${jsonDoc.firstname}"
			println( "Fullname: $fullName")
			assert jsonDoc.fullname == fullName
			
			
			for (def p in doc.getProperties()) println("${p.key}='${p.value}'")

			int newDDPValue = doc.getProperty(DDP_Name) as int
			assert  newDDPValue ==  ddpValues[docNo]+1
			println("${DDP_Name}=${newDDPValue}")
		}
	}
}
