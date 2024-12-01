# Demo All - Process Script

## The Process Script

The `DemoAll` process script demos many Boomi Script features, like

* reading and writing documents,
* JSON parsing,
* Logging,
* dynamic document properties,
* dynamic process properties,
* process properties
* and execution context.

## The Test

The Test demos various testing scenarios, like creating documents from code or loading them from files within a directory (using `new TestFilesHelper("testData", _sourceUri))`. It demos how to create Dynamic Document, Dynamic and Process Properties in a Test.

The test also contains some assertions, for example, how to test a certain property was set correctly by the script: 
```groovy
assert !idocType.isEmpty(), "DDP_IDocType must be empty in case of success!"
```

### Advanced Groovy

Groovy fans might want to look at
```groovy
private static String getPropertyOrDefault(
        Document doc, 
        String propertyName, 
        Closure<String> unknownProperty = null) { /* ... */ }
```

as an example, hot to use _Closures_ and optional method parameters.



## The documentation

The process script contains an inline documentation that should be part of very script!
```Groovy
/* **************************************************************************
    {One line description}
   
    {Longer Text}
     
    IN:
    OUT:            
    ------------------------------------------------
    27.04.2024  mspro   -   Created
************************************************************************** */
```

And the test folder contains this `readme.md` (Markdown) that should give you a quick overview of everything.