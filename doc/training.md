# ScriptEase Training Sessions

## Preparation

* Walk through [Software Installation | The MSPro Boomi Collection](https://mspro.gitbook.io/the-mspro-boomi-collection/boomi-scriptease/pre-requisites-7deb11c4cf894c33b76456ab85cad596) and install all necessary software on your machine
* Setup the **MyScripts** project: [Project Setup | The MSPro Boomi Collection](https://mspro.gitbook.io/the-mspro-boomi-collection/boomi-scriptease/setup-a-customer-project-a5e8a967b06b4f9d9123b55f72e07145)

At the end of the preparation, you should have [verified your project setup.](https://mspro.gitbook.io/the-mspro-boomi-collection/boomi-scriptease/setup-a-customer-project-a5e8a967b06b4f9d9123b55f72e07145/verify-project-setup)

## Day 1

* some resources 
* [Learn Groovy | The MSPro Boomi Collection](https://mspro.gitbook.io/the-mspro-boomi-collection/boomi-scriptease/knowlede-base/learn-groovy)
* We play!
```groovy
println "Hello world!"
String name = "Markus"
println("Hello $name")
```

```groovy
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

println "Hello world!"
String name = "Markus"
println("Hello $name")


def js = new JsonSlurper()
def json = js.parseText('''
  { "firstname" : "Markus",  
    "lastname" : "Schmidt" }
''')

// See output with [] --> Map = KeyValue Array/List
println(json)
println("-----")

String text = JsonOutput.toJson(json)
println(JsonOutput.prettyPrint(text))
```

  * ctrl k, f
  * def, bulb, explicit type
  * Variables, *def*, Strings ' ""'
  * 2.3.1. Declaring classes
    * Listing 2.3. Using the Book class from a script




  * Arrays, Maps, JsonSlurper

* [Concepts | The MSPro Boomi Collection](https://mspro.gitbook.io/the-mspro-boomi-collection/boomi-scriptease/general)
  * [1 - Debug an existing process script | The MSPro Boomi Collection](https://mspro.gitbook.io/the-mspro-boomi-collection/boomi-scriptease/examples/1-debug-an-existing-process-script)
    * Talk about the basics, Tests and Scripts
    * See IDE and how to debug, watch variables
    * *Run* and *Debug*
  * Add logging to the "Empty" script
    * paste it into Boom and let it run
  * Goto ScriptContext creation
* Walk through different Process Context scenarios.
  * Add the documentFromText() method
  * and debug
  * Let the team add a getProcessProperty( "DPP_TestProp") and assign it to a string
* Fix the "Empty" project and log "DPP_TestProp" 
  * copy and paste back to Boomi, set DPP_TestProp and see logs

## Extension

* Files from disk
* read from xml
* set DDP