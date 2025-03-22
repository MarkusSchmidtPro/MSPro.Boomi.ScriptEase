import msPro.services.LicenseManager
import org.junit.Test

class LicenseActivator {

    /* 
     * Paste your license and 
     * run 'activateLicense()`- click on the green arrow
     */
    static final String yourLicense = "e27e4585-90e5-4df3-b475-887f8195da38"


    @Test
    void activateLicense() {
        def licenseManager = new LicenseManager()
        println("Your UserName: '${licenseManager.getUserName()}'")
        licenseManager.activateLicense(yourLicense)
        // Code=2 : Invalid License-Key
        // Code=3 : License is in use by another user
    }
}