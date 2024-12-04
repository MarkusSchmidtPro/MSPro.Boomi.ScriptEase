package mapScript.conditionalMapping


import groovy.transform.SourceURI
import groovy.transform.TypeChecked
import msPro.scriptease.MapScript
import msPro.scriptease.ProcessContext
import org.junit.Before
import org.junit.Test

@TypeChecked
class Test_conditionalMapping extends GroovyTestCase {
    final String SCRIPT_NAME = "conditionalMapping"

    @SourceURI
    URI _sourceUri
    MapScript _testScript

    /** Create the script instance
     */
    @Before
    void setUp() { /* see test01() */  }


    @Test
    void test01() {
        /*
            Process Properties are set "per Process" - they are part of the process context! 
         */
        // The process property component Id
        final String ppComponentId = "281434d2-f9d8-4700-905f-3acce983d3bf"
        // The property key (id) that will contains a comma separated list of VKORGs
        final String ppKeyVKOrgRegion = "7f244535-8805-4022-aab3-a636c035331e"

        ProcessContext context = new ProcessContext(
                procProps: [
                        // Create a process property on the process context.
                        (ppComponentId): [
                                (ppKeyVKOrgRegion): "2090,9999",
                        ]
                ])
        _testScript = new MapScript("msg" + SCRIPT_NAME + ".groovy", _sourceUri, context)

        Map variables = _testScript.run([VKORG: "2090", REGIO: "RE"])
        assert variables.region == "RE"

        variables = _testScript.run([VKORG: "5555", REGIO: "XY"])
        assert variables.region == ""
    }
}	