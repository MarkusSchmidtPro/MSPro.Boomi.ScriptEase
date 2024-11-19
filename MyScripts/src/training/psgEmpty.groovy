package training

import com.boomi.execution.ExecutionUtil

/*  An empty Groovy Process Script, 
    that does nothing but passing though all documents.
    
    NOTE: The Groovy Script language does not require semicolon at the end of each statement,
    and to avoid unexpected situations, we should really not use it.
 */
for(int i = 0; i < dataContext.getDataCount(); i++ ) {
    InputStream is = dataContext.getStream(i);
    Properties props = dataContext.getProperties(i);
    
    dataContext.storeStream(is, props);
}