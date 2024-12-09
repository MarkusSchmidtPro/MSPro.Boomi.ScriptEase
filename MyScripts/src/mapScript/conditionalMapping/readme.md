# Conditional Mapping

The _CityList_ example demonstrates a lot of Groovy List, Map and Test functionality, as well as, strict typing and some good practices.  

## Script Functionality 

The script gets a comma separated list of **VKORG**s from a process property (ComponentId+Key). This lists represents the **known Sales Organization Numbers** (VKORG [German, SAP] _Verkaufsorganization_).

It checks if the current [VKORG] - provided as a map input - is part of that known VKORGs list.

* If YES the output [region] is set to the input [REGION]
* If NO the output [region] is empty.

### Example
pp.VKORG = "2090,9999"

#### Input IDoc Document
```xml
<?xml version="1.0" encoding="UTF-8"?><DEBMAS07>
  <IDOC BEGIN="1">
    <EDI_DC40 SEGMENT="1">
      <TABNAM>EDI_DC40</TABNAM>
   ..
      <SERIAL>20240325111625</SERIAL>
    </EDI_DC40>
    <E1KNA1M SEGMENT="1">
      <MSGFN>005</MSGFN>
      <KUNNR>0010034884</KUNNR>
      ...
      <SPRAS_ISO>NL</SPRAS_ISO>
     ....
      </E1KNA1H>
      <E1KNVVM SEGMENT="1">
        <MSGFN>005</MSGFN>
        <VKORG>2090</VKORG> <<<<--------------
        <VTWEG>10</VTWEG>
      ...
  </IDOC>
</DEBMAS07>
```