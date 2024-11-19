package training.aggregatePrices

import com.boomi.execution.ExecutionUtil
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

final String SCRIPT_NAME = "AggregatePrices"

/* **************************************************************************
    Aggregate prices on JSON documents.
        
    IN : JSON documents of profile j.Price
         { "articleNo" : "a001", "price" : 1.23 }
         
    OUT: JSON documents of profile j.Prices (plural=array)
        [
		    {
		        "articleNo" : "a001",
		        "maxPrice"  : 1.23, "minPrice"  : 0.98, "priceCount" : 3,
		        "prices" : [ 1.23, 1.05, 0.98 ]
		    },{
		        "articleNo" : "a002",
		        "maxPrice"  : 2.00, "minPrice"  : 2.00, "priceCount" : 1,
		        "prices" : [ 2.00 ]
		    }
		]
    
    ------------------------------------------------
    13.06.2024  mspro -   Created
    Template v0.2.1
************************************************************************** */


final _logger = ExecutionUtil.getBaseLogger()
_logger.finest('>>> Script start ' + SCRIPT_NAME)

def js = new JsonSlurper()

try {
	int docCount = dataContext.getDataCount()
	_logger.fine("In-Document Count=" + docCount)

	// https://www.tutorialspoint.com/groovy/groovy_lists.htm
	List articles = []

	for (int docNo = 0; docNo < docCount; docNo++) {
		final String textDoc = _getTextDocument(dataContext, docNo)
		final Properties props = dataContext.getProperties(docNo)

		// *********** Document related functionality ************

		Map jsonDoc = js.parseText(textDoc)  as Map
		_logger.info("DOC[$docNo]: ${jsonDoc.articleNo} = ${jsonDoc.price}")

		// Business Logic

		// Check if there is already an item with the same articleNo.
		// If yes, we must update that item. 
		// If no, we create a new item and add it to the articles result list.
		// it - iterator = List element (of type)

		def article = articles.find { it.articleNo == jsonDoc.articleNo }
		// article TYPE 
		// {    
		//      articleNo
		//      minPrice, maxPrice, priceCount, 
		//      articles[]
		// }

		if (article == null) { // << set breakpoint here
			articles.add([
					articleNo: jsonDoc.articleNo,
					priceCount : 1,
					minPrice : jsonDoc.price,
					maxPrice : jsonDoc.price,
					prices :  [ jsonDoc.price] 
			])
		} else {
			// article found -> update
			article.priceCount++        // increment
			if( jsonDoc.price < article.minPrice) article.minPrice = jsonDoc.price 
			if( jsonDoc.price > article.maxPrice) article.maxPrice = jsonDoc.price
			article.prices.add(jsonDoc.price ) // We do _not_ check if the same articles already exists!
		}

		// ******** end of Document related functionality ********

	}   // documents loop
	
	// Your process related code (process properties etc.) here
	String outputDoc = JsonOutput.toJson(articles)
	_setTextDocument(dataContext, outputDoc, new Properties())
}
catch (Exception e) {
	_logger.severe(e.message)
	throw e
}

// =================================================
// -------------------- LOCALS ---------------------
// =================================================

static String _getTextDocument(dataContext, int docNo) {
	InputStream documentStream = dataContext.getStream(docNo)
	return documentStream.getText("UTF-8")
}

static void _setTextDocument(dataContext, String value, Properties props) {
	InputStream newDocumentStream = new ByteArrayInputStream(value.getBytes("UTF-8"))
	dataContext.storeStream(newDocumentStream, props)
}
