//file:noinspection GroovyDocCheck
package mapScript.helloWorld

import groovy.transform.*
import msPro.scriptease.*
import org.junit.*


/**
 * A Test class that contains one or more test methods to test your script.
 *
 * For more information on testing see: 
 * @see https://junit.org/junit4" 
 */
@TypeChecked
class Test_HelloWorld {

	final String SCRIPT_NAME = "HelloWorld"

	@SourceURI
	URI _sourceUri
	MapScript _testScript

	/** Setup process related values.
	 *
	 * On the Atom a Map script runs in the context of a Boomi process (execution).
	 * For each execution, the script is instantiated only once (on first use in the process). 
	 * Subsequent calls to the same script are made on the same instance but with a different 
	 * DataContext.
	 * For that reason, we create the script instance before the tests. 
	 * Process related (and execution) properties are also specified before the tests as the @ProcessContext.
	 *
	 */
	@Before
	void setUp() {
		ProcessContext context = null
		/* OPTIONAL: Specify a ProcessContext with 
			* Execution Properties       : execProps
			* Dynamic Process Properties : dynProcProps
			* Process Properties         : procProps
			
			context = new ProcessContext(
					dynProcProps: ["DPP_01": "Value01"]
			)
		*/

		// Create the script instance
		_testScript = new MapScript("msg" + SCRIPT_NAME + ".groovy", _sourceUri, context)
	}

	/** Your first Map Script Test. */
	@Test
	void test01() {
		//
		// A Map Script Test provides the mapping input parameters - as they are defined in Boomi - to the run() method.
		// Script Output variables (which include the input variables) are returned by the run() function 
		// and can be validated after the execution.
		//
		final Map inputVariables = [a: 5, b: 7]
		final int expectedTotal = inputVariables.a + inputVariables.b

		Map<String, Object> variables = _testScript.run(inputVariables)

		println("\r\n--- Test Output ----------")
		assert variables.total != null, "Script did not set 'total' as output parameter!"
		assert variables.total == expectedTotal, "Calculation result does not meet expectations!"

		// Print to console windows and validate results
		println("Test Total = " + variables.total)
	}
}
