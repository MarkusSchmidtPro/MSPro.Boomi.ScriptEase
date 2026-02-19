package mapScript.dateTime

import groovy.transform.*
import msPro.scriptease.*
import org.junit.*

@TypeChecked
class Test_DateTimeParse extends GroovyTestCase {
    final String SCRIPT_NAME = "DateTimeParse"


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
        Map variables = _testScript.run([
                dateTimeString: "2007-04-16 00:00:00.0",
                inputFormat: "yyyy-MM-dd HH:mm:ss.S"
		])

		println("\r\n--- Test Output ----------")
		 //assert variables.dateTimeFormatted == "20251103 120358.380", "Script did not set 'total' as output parameter!"
		 println("Test Total = " + variables.dateTimeFormatted)
	}
}	