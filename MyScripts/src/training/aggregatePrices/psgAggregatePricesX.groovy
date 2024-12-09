package training.aggregatePrices

import com.boomi.execution.ExecutionUtil
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

final String SCRIPT_NAME = "AggregatePricesX"

/* **************************************************************************
    Aggregate prices on JSON documents.
        
    IN : XML documents of profile j.Price
         <root>
			<articleNo>a001</articleNo>
			<price>1.23</price>
		</root>
         
    OUT: XML document
   <root>
	  <Article no="a001" priceCount="3" minPrice="0.98" maxPrice="1.23">
		<Price>1.23</Price>
		<Price>1.02</Price>
		<Price>0.98</Price>
	  </Article>
	  <Article no="a002" priceCount="1" minPrice="2.00" maxPrice="2.00">
		<Price>2.00</Price>
	  </Article>
	</root>
           
    ------------------------------------------------
    09.12.2024  mspro -   Created
    Template v0.2.1
************************************************************************** */


final _logger = ExecutionUtil.getBaseLogger()
_logger.info('>>> Script start ' + SCRIPT_NAME)

def xs = new XmlSlurper()

try {
	int docCount = dataContext.getDataCount()
	_logger.fine("In-Document Count=" + docCount)

	// Groovy supports two different ways of building XML:
	// a) XmlSlurper and b) XmlParser - https://www.baeldung.com/groovy-xml
	// I recommend using XmlSlurper for parsing (reading) XML, only, and using
	// XmlParser for writing Xml data.

	// Create a new XmlParser and build the XML structure
	def outXmlDoc = new Node(null, 'root')

	for (int docNo = 0; docNo < docCount; docNo++) {
		final String textDoc = _getTextDocument(dataContext, docNo)
		final Properties props = dataContext.getProperties(docNo)

		// *********** Document related functionality ************

		def xDocRootNode = xs.parseText(textDoc)  
		_logger.info("DOC[$docNo]: ${xDocRootNode.articleNo} = ${xDocRootNode.price}")

		// Business Logic

		// Check if there is already an item with the same articleNo.
		// If yes, we must update that item. 
		// If no, we create a new item and add it to the articles result list.
		// it - iterator = List element (of type)

		def articleNode = outXmlDoc.Article.find { 
			Node n -> n.'@no' == xDocRootNode.articleNo 
		}
		// article TYPE 
		// {    
		//      articleNo
		//      minPrice, maxPrice, priceCount, 
		//      articles[]
		// }

		if (articleNode == null) { // << set breakpoint here
			// build a new Node => Article Object
			articleNode = new NodeBuilder().Article( 
					no: xDocRootNode.articleNo ,
					priceCount: 1,
					minPrice: xDocRootNode.price ,
					maxPrice: xDocRootNode.price ,
			) {
				Price(xDocRootNode.price)
			}
			outXmlDoc.append(articleNode)
		} else {
			// article found -> update
			articleNode.@priceCount ++        // increment
			if( xDocRootNode.price.toDouble() < articleNode.@minPrice.toDouble() ) articleNode.@minPrice = xDocRootNode.price 
			if( xDocRootNode.price.toDouble() > articleNode.@maxPrice.toDouble()) articleNode.@maxPrice = xDocRootNode.price

			Node priceNode = new NodeBuilder().Price(xDocRootNode.price)
			articleNode.append( priceNode ) 
		}

		// ******** end of Document related functionality ********

	}   // documents loop
	
	// Your process related code (process properties etc.) here
	String outputDoc = _serializeXml( outXmlDoc)
	_setTextDocument(dataContext, outputDoc, new Properties())


}
catch (Exception e) {
	_logger.severe(e.message)
	throw e
}

// =================================================
// -------------------- LOCALS ---------------------
// =================================================

static String _serializeXml( Node xmlDoc){
	def writer = new StringWriter()
	def printer = new XmlNodePrinter(new PrintWriter(writer))
	printer.setPreserveWhitespace(true) // Keep whitespace as it is
	printer.print(xmlDoc)
	return writer.toString()
}


static String _getTextDocument(dataContext, int docNo) {
	InputStream documentStream = dataContext.getStream(docNo)
	return documentStream.getText("UTF-8")
}

static void _setTextDocument(dataContext, String value, Properties props) {
	InputStream newDocumentStream = new ByteArrayInputStream(value.getBytes("UTF-8"))
	dataContext.storeStream(newDocumentStream, props)
}
