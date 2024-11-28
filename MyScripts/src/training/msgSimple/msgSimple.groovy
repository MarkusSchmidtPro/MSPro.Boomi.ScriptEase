package training.msgSimple

final String SCRIPT_NAME = "Simple"

/** ================================================================================== 
 A very simple map script, that build a fullname based on first- and lastname.

 IN : [Describe inbound variables]
 a       : A firstname
 b       : A lastname [mandatory]

 OUT: [Describe outbound variables]
 fullName 
 
 ------------------------------------------------
 23.11.2024  mspro -   Created
 ==================================================================================
 */
/* TIP
    Throwing an Exception in a map-script will throw an Exception in 
    the process on the document that is currently mapped!
    Without an exception handler (try/catch) in the process, the process will stop and fail.
    
    If you put a try/catch (somewhere) before the Map shape, only the failing document is 
    routed down into the exception handler.
    try/catch ---- + ---> Map --> Return Documents (successfully mapped)
                   |
                   +---> Failed Documents (e.g. because of an Exception in the Map Script)
                   
    This is a great pattern to deal with unexpected (invalid) data directly in the map-script.  
*/

if( lastname == null || lastname.isBlank())
    throw new Exception( "Mandatory {lastname} is empty!")

/*  TIP
    We assign the (untyped) input to explicit String variables to get intelli-sense support from the IDE.
    Without providing an explicit type, the IDE cannot know the variable's type,
    and therefore it cannot provide suggestions on it.
*/
String _lastname = lastname
String _firstname = firstname

if( _firstname == null || _firstname.isBlank()){
    fullname = _lastname
}
else {
    fullname = "$lastname, $firstname"
}