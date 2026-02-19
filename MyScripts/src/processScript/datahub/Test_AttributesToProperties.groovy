package processScript.datahub

import groovy.transform.*
import msPro.scriptease.*
import org.junit.Test

@TypeChecked
class Test_AttributesToProperties extends GroovyTestCase {
    final String SCRIPT_NAME = "AttributesToProperties"

    @SourceURI
    URI _sourceUri
    final ProcessScript _testScript = new ProcessScript("psg" + SCRIPT_NAME + ".groovy", _sourceUri)
    final TestFilesHelper _testFiles = new TestFilesHelper("testData", _sourceUri)

    /** A short description what this test is supposed to do. */
    @Test
    void test01() {
        // Initialize the Script Execution Context:
        // * Execution Properties       : executionProperties
        // * Dynamic Process Properties : dynProcProps
        // * Process Properties         : procPros
        // * Documents                  : inputDocuments
        //      incl. Dynamic Document Properties
        // --------------------------------------------------------------

        ProcessScriptContext context = new ProcessScriptContext(
                inputDocuments: [
                        Document.fromText('''
                            <idtest createddate="02-16-2026T09:35:50.000+0000" 
                                grid="59c7cd73-0d25-4a0a-a86a-9b69f7745b86" 
                                updateddate="02-17-2026T09:06:33.000+0000" 
                                source="NAVBC">
                              <id>59c7cd73-0d25-4a0a-a86a-9b69f7745b86</id>
                              <name>Company 001 - EFX.1</name>
                              <name2>NAME_2</name2>
                              <zipcode>1234</zipcode>
                              <uniquekeys>
                                <vatno>V_5001</vatno>
                                <crefono>CRE01</crefono>
                              </uniquekeys>
                            </idtest>
                        '''),
                        Document.fromText('''
                            <idtest createddate="02-16-2026T09:35:50.000+0000" grid="59c7cd73-0d25-4a0a-a86a-9b69f7745b86" updateddate="02-17-2026T13:10:47.000+0000" source="NAVBC"><id>59c7cd73-0d25-4a0a-a86a-9b69f7745b86</id><name>Company 001 - EFX.3</name><name2>NAME_2</name2><zipcode>1234</zipcode><uniquekeys><vatno>V_5001</vatno><crefono>CRE01</crefono></uniquekeys></idtest>
                        ''')
                        //, Document.fromFile( _testFiles.get( "filename.XML"))
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
