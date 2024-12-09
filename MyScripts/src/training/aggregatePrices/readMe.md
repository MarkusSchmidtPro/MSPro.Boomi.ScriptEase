# Aggregate Prices

This script is documented in the [Aggregate Prices Example | The MSPro Boomi Collection (gitbook.io)](https://mspro.gitbook.io/the-mspro-boomi-collection/boomi-scriptease/examples).

## The Boomi Process

:exclamation: **IMPORTANT**
The Boom Process must run on a local ATOM because we are reading files from the local disk!

Build a Boomi Integration Test process which uses the *Aggregate Prices* Script with the same test-data files as this script. Test-data is located in `{projectDir}\src\training\aggregatePrices` (project directory is where the *IML* files resides) and there are four files:

<img src=".assets/image-20241209085103770.png" alt="image-20241209085103770" style="zoom:50%;" />

## Process Overview

This is how the complete process will look like:

![image-20241209091724665](.assets/image-20241209091724665.png)

* Configure Disk v2 Connector
* Query files from local disk
* Execute Script with the example documents

## 1 - Process Property for Configuration

The *trick* here is that the ATOM reads the test files from the same directory as the local script project: they share test data! To make this a bit more comfortable (configurable) we use a Process Property for the Training.

<img src=".assets/image-20241209090525241.png" alt="image-20241209090525241" style="zoom:50%;" />

| Name                     | Description                                                  |
| ------------------------ | ------------------------------------------------------------ |
| `pp.Training`            | A Process Property that is used for training purposes.       |
| Key: `ScriptProjectRoot` | The absolute directory path of the Script IntelliJ project (IML files location). |

### Extensions

Leave the Default value to **SET BY EXTENSION**, we are going to provide the directory when we test (see below):

<img src=".assets/image-20241209091233984.png" alt="image-20241209091233984" style="zoom:50%;" />



## Built the process

### Configure Disk v2 Connector

Caption: *set DiskV2.Directory = {pp.Training}\....\testData*
**Disk v2 - Directory**: pp.Training.ScriptProjectRoot + Relative Path to the test-data directory

![image-20241209092519673](.assets/image-20241209092519673.png)

### Query Files

Use the most generic Disk v2 Connector you can:

<img src=".assets/image-20241209092754473.png" alt="image-20241209092754473" style="zoom:50%;" />

with a `disk.queryWildcard`operation:

<img src=".assets/image-20241209093008103.png" alt="image-20241209093008103" style="zoom:50%;" />

The `matchWildcard` file-filter is then provided as a Shape Parameter:

<img src=".assets/image-20241209093744100.png" alt="image-20241209093744100" style="zoom:50%;" />

Finally add the Script (Data Process Shape) and **Test** it.

![image-20241209093136299](.assets/image-20241209093136299.png)

Do not forget to extend the Training Process Property when you execute the process:

<img src=".assets/image-20241209091526675.png" alt="image-20241209091526675" style="zoom:50%;" />

If you did it right, you will see the four documents which we used or local script testing flowing to your Data Process Shape.

![image-20241209094046601](.assets/image-20241209094046601.png)
