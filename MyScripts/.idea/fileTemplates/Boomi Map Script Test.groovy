package templatesSource

import groovy.transform.*
import msPro.scriptease.*
import org.junit.*

@TypeChecked
class Test_${Plain_BoomiScriptName} extends GroovyTestCase {
	final String SCRIPT_NAME = "${Plain_BoomiScriptName}"


	@SourceURI
	URI _sourceUri
	MapScript _testScript

	/** Create the script instance
	*/
	@Before
	void setUp() {
		_testScript = new MapScript("msg" + SCRIPT_NAME + ".groovy", _sourceUri)
	}

	
	@Test
	void test01() {
		Map variables = _testScript.run( [
			a: 5, 
			b: 7
		])

		println("\r\n--- Test Output ----------")
		// assert variables.total != null, "Script did not set 'total' as output parameter!"
		// println("Test Total = " + variables.total)
	}
}	