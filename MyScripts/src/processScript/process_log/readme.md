# Trace - Process Script

## Logging

The main purpose of this script was to demo how to use a `Logger``. 
Logging is extremely important. When your script runs on the platform, logging is the only chance to visualize what it does. I recommend logging input and output parameters, at least.

## Unit Testing

A second topic, which is not less important, that is demoed here is Unit Testing. In the test class  `class Test_ProcessScript_Simple` you will find **two** test methods!

> **Note:** You can run the test class, or you can run a single test method (focused testing and/or debugging). You can even run _All Tests_ in the project to ensure **all** your scripts meet your expectations.
 
Normally you start with _one_ test method. You will use this first test method to develop a script under certain (test) conditions. Once your first release is ready to go, you will never ever touch this first _test00_ method again. As long as the script's requirements haven't changed, the test's "assertions" and expectations are always true, and no matter what you change in the script, _test00_ must still succeed! What you do instead: you create a new test method _test01_ with new inputs and different assertions, to test different scenarios, for example, edge tests.

Over the time, when the script requirements may change (increase), add tests as necessary. You can even build new test classes - not only methods - all with the same test subject: you script. You expect all tests to succeed at any time to ensure the integrity of your script. 
