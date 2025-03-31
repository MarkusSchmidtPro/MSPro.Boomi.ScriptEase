import msPro.services.LicenseManager
import org.junit.Test

class LicenseActivator {

    /* 
     * Paste your license and 
     * run 'activateLicense()`- click on the green arrow.
     * ----------------------------------------------------
     * WARN: Could not validate a License!
     * This may happen, when the licensing service was inactive for some time. 
     * --> Please wait a minute or two and try again! <--
     * ----------------------------------------------------
     * ERROR License validation failed -> Markus@MarkusSchmidt.pro
     *  java.lang.Exception: License validation failed! Code=2 <<<<<<<<<<<<
        // Code=2 : Invalid License-Key
        // Code=3 : License is in use by another user
     * 
     * ----------------------------------------------------
     * https://boomi.markusschmidt.pro/boomi-scriptease/licensing/license-activation
     */
    static final String yourLicense = "..."


    @Test
    void activateLicense() {
        def licenseManager = new LicenseManager()
        println("Your UserName: '${licenseManager.getUserName()}'")
        licenseManager.activateLicense(yourLicense)
    }
}