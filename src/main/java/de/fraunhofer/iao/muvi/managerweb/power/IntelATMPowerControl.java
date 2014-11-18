package de.fraunhofer.iao.muvi.managerweb.power;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.fraunhofer.iao.muvi.intel.management.wsman.DefaultAuthenticator;
import de.fraunhofer.iao.muvi.intel.management.wsman.ManagedInstance;
import de.fraunhofer.iao.muvi.intel.management.wsman.ManagedReference;
import de.fraunhofer.iao.muvi.intel.management.wsman.WsmanConnection;

public class IntelATMPowerControl {
	
	private static final Log log = LogFactory.getLog(IntelATMPowerControl.class);
	

/**
* Connection object to the Intel(r) AMT Client
*/
  protected WsmanConnection connection;
/**
* Default authenticator
*/
  protected DefaultAuthenticator defaultAuth;

// Connction Properties

/**
* Enable / Disable the use of TLS over the connection
*/
   protected boolean tls=false;

/**
* Enable/Disable Kerberos authentication
*/
   protected boolean kerberos=false;

/**
* Fullpath to the Kerberos configuration file
*/
   protected String  kerbConfFile="";
/**
* Fullpath to Kerberos login configuration file
*/
   protected String  loginConfFile="";
/**
* Enable / Disable verbose output
*/
   protected boolean verbose=false;
   
   /*Default constructor, only setup the connection here*/ 
   public IntelATMPowerControl(String dcIp, String user, String pass){
	   OpenConnection(dcIp,user,pass);
   }
	
  /**
    * Open a connection to the Intel(r) AMT Client using the properties passed on the command line.
    */
   public void OpenConnection(String dcIp, String user, String pass){
        //Step 1 - Create a connection to the AMT Client

        // build the connection string/url
        String url = "http://";
        String port = ":16992/wsman";
        	        
        url = url + dcIp;
        url = url + port;

        // create the connection
        connection = WsmanConnection.createConnection(url);

        //Step 2 - Setup the selected authentcation

        //Create a default authenticator
        DefaultAuthenticator auth = new DefaultAuthenticator();

        //associate the authenticator with Java runtime
        java.net.Authenticator.setDefault(auth);
 
        //Add the digest credentials to the default authenticator
        auth.addCredential(url, "digest", user, pass);


    }
	
	   
	   /**
	     * Get the current power state of the Intel(r) AMT Client
	     */
    protected String getPowerState() {
		int powerState;
		ManagedReference associatedPMS_ref;
		ManagedInstance APMS;
		
		try {
			associatedPMS_ref = connection.newReference("CIM_AssociatedPowerManagementService");
			APMS = associatedPMS_ref.get();
			powerState = Integer.parseInt(APMS.getProperty("PowerState").toString());
			return translatePowerState(powerState);
		}
		catch (Exception e) {
			log.error("Exception: " + e.getMessage(), e);
			return "-1";
		}
	}
    
    /**
     * Translate the given power state in human readable form
     *
     * @param PowerState power state to be translated
     */
	protected String translatePowerState(int PowerState) {
		String[] values = { "Other", "On", "Sleep - Light", "Sleep - Deep", "Power Cycle (Off - Soft)", "Off - Hard", "Hibernate (Off - Soft)", "Off - Soft" };
		
		if (PowerState >= 1 && PowerState <= 8) {
			return values[PowerState - 1];
		}
		else {
			return null;
		}
	}
	
	 /**
     * Power up the Intel(r) AMT Client
     */
	protected boolean powerUp() {
		
		int response = requestPowerStateChange(2);
		
		if(response == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	protected int requestPowerStateChange(int PowerState) {
		ManagedReference computerSystem_ref, powerMS_ref;
		ManagedInstance requestPSC_IN, requestPSC_OUT;
		
		try {
			computerSystem_ref = connection.newReference("CIM_ComputerSystem");
			computerSystem_ref.addSelector("Name", "ManagedSystem");
			computerSystem_ref.addSelector("CreationClassName", "CIM_ComputerSystem");
			
			powerMS_ref = connection.newReference("CIM_PowerManagementService");
			
			requestPSC_IN = powerMS_ref.createMethodInput("RequestPowerStateChange");
			requestPSC_IN.setProperty("PowerState", Integer.toString(PowerState));
			requestPSC_IN.setProperty("ManagedElement", computerSystem_ref);
			requestPSC_OUT = (ManagedInstance)(powerMS_ref.invoke(requestPSC_IN));
			return Integer.parseInt(requestPSC_OUT.getProperty("ReturnValue").toString());
		}
		catch (Exception e) {
			log.error("Exception: " + e.getMessage(), e);
			return -1;
		}
	}
	
	 /**
     * Reset the Intel(r) AMT Client
     */
	protected int reset() {
		return requestPowerStateChange(10);
	}

}
