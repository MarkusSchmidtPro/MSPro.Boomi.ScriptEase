package training.msgSimple

import groovy.json.JsonSlurper
import groovy.transform.SourceURI
import msPro.scriptease.MapScript
import msPro.scriptease.TestFilesHelper
import org.junit.Before
import org.junit.Test


class Test_Simple extends GroovyTestCase{
    final String SCRIPT_NAME = "Simple"


    @SourceURI
    URI _sourceUri
    MapScript _testScript

    final TestFilesHelper _testFiles = new TestFilesHelper("testData", _sourceUri)

    /** Create the script instance
     */
    @Before
    void setUp() {
        _testScript = new MapScript("msg" + SCRIPT_NAME + ".groovy", _sourceUri)
    }


    @Test
    /** This test runs the map script with (three) different input sets
     *  and validates all output meets our expectations. 
     */
    void test01() {
        /*  TIP!
            The DataContext does not support types.
            
            If you get:
                Cannot find matching method java.lang.Object#plus(java.lang.String). 
                Please check if the declared type is right and if the method exists.
                
                The issue is the `@TypeChecked` attribute on the test class which enforced types!
                Simply remove that attribute from the class .
         */
        File f = _testFiles.get("doc01.json")
        def j1 = new JsonSlurper().parse(f) as Map
        Map dataContext1 = _testScript.run([
                firstname: j1.firstname,
                lastname : j1.lastname
        ])
        assert dataContext1.fullname == dataContext1.lastname + ", " + dataContext1.firstname

        
        Map dataContext2 = _testScript.run([
                firstname: null,
                lastname : "Schmidt",

        ])
        assert dataContext2.fullname == dataContext2.lastname

        shouldFail { // https://blog.mrhaki.com/2009/11/groovy-goodness-testing-for-expected.html
            Map dataContextInvalid = _testScript.run([
                    firstname: "Markus",
                    lastname : null
            ])
        }


        //println("\r\n--- Test Output ----------")
        // assert variables.total != null, "Script did not set 'total' as output parameter!"
        // println("Test Total = " + variables.total)
    }
}	