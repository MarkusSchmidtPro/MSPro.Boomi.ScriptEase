# JSON Demo

## Script Functionality 

The script takes any number of JSON documents of type:

```json
{
  "name" : "Markus Schmidt",
  "address" : {
    "city" : "Wiesbaden",
    "street" : "Niedernhausener Str. 59a"
  }
}
```
 
it builds _one output document_ with an alphabetically ordered list of cities and their citizens:

```json
[
  {
    "city" : "Munich",
    "citizens" : [
      { "name" : "Markus Schmidt", "street" : "def"  },
      { "name" : "Lisa Meyer", "street" : "ghi"  },
      { ... }
    ]
  },
  { "city2" :  "Wiesbaden",
    "citizens" : []]
  }
```

Finally, we want to have a `DDP_CityCount` that contains the number of cities. 

## Some advice
 
* Create a _Boomi Process Script_ from a template, and call it: _CityList_
* Create a _Boomi Process Script Test_ from a template, and refer to the _CityList_ script.
* Amend the lines `ProcessScriptContext( inputDocuments: ..` to read the four test documents form file.
* Set a breakpoint ont ` _testScript.run(context)` and debug the script.
  * See if it compiles and stops, check the `inputDocuments`.

The test setup is complete - even if there are no assertions yet, the script is called and can be developed!

* Implement the functionality!

When you are done, or when you have developed something meaningful, run the test and debug your code.

### Fix assertions
By default, there is an assertion in the Test that checks that the output document count equals the input document count. That assertion is no longer correct because we expect exactly one output document. 
* In the Test, fix the document count assertion to expect only one document: `ssert 1 == docCount`.
 