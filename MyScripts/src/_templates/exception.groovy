package _templates



//region Document Exceptions

/*  Exception Pattern Code Snippet

    // *********** Document related functionality ************
    try {
        _setDDP(props, "DDP_Error", '')
        // *********** Safe functionality ************
        
        //if (docNo % 2 == 0)
        //    throw new DocumentException(docNo, "Document No $docNo is even!")

        // *********** end of Safe functionality ************
    } catch (templates.DocumentException docEx) {
         _setDDP(props, "DDP_Error", 
            "Exception: templates.DocumentException\r\n" + JsonOutput.prettyPrint(JsonOutput.toJson(docEx.AsJson())))
    }
    // ******** end of Document related functionality ********
*/



/** Represents an Exception that occurred during document processing.
 *
 * Use ths exception in a process script's document loop 
 * in case any exception occurred on the current document:
 * `if (docNo % 2 == 0) throw new templates.DocumentException(docNo, "Document No $docNo is even!")`.
 */
class DocumentException extends RuntimeException {
    private final String _errorMessage
    private final int _docNo

    DocumentException(int docNo, String message) {
        _errorMessage = message
        _docNo = docNo
    }

    String getErrorMessage() { return _errorMessage }

    int getDocNo() { return _docNo }

    Map AsJson() { return [errorMessage: errorMessage, docoNo: docNo] }
}

//endregion
