package de.fraunhofer.iao.muvi.managerweb.power;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import de.fraunhofer.iao.muvi.managerweb.backend.Database;

/**
 * This is the StartStop class only a single  instance of this class is needed to control the IPower 
 * that is connected to the displays of cloud wall<br>
 * <br>
 *  Usage<br>
 *  
 *  StartStop iPower = new StartStop();<br>
 *  
 *  iPower.powerStatus(); //will return the status of all 8 connections<br>
 *  
 *  iPower.startUpIPower(); // will start up all the monitors in sequence and will take total 80 sec<br>
 *  
 *  iPower.shutDown();  // will shutdown all the 8 OP<br>
 *
 */
public class StartStop {
	
	private static final Log log = LogFactory
			.getLog(StartStop.class);
	private Database database;
	
	
	Path currentRelativePath = Paths.get("");
	String path = currentRelativePath.toAbsolutePath().toString();
	private String powerIp ;
	private String s ;
	private String GET;
	private String SET;
	
	private String user;
	private String pass;
	
	
	protected String  bootUser="admin";
	protected String  passDc1="Admin.DC1";
	protected String  passDc2="Admin.DC2";
	protected String  passDc3="Admin.D3";
	protected String  passDc4="Admin.D4";
	
	private  String DC1Ip;
	private  String DC2Ip;
	private  String DC3Ip;
	private  String DC4Ip;
	
	//The different outlets on the IPower device each should have values only 0 or 1
	private static String startUpA = "\"1,1,1,1,1,1,1,1\"";
	private static String startUpB = "\"0,1,1,1,1,1,1,1\"";
	private static String startUpC = "\"0,0,1,1,1,1,1,1\"";
	private static String startUpD = "\"0,0,0,1,1,1,1,1\"";
	private static String startUpE = "\"0,0,0,0,1,1,1,1\"";
	private static String startUpF = "\"0,0,0,0,0,1,1,1\"";
	private static String startUpG = "\"0,0,0,0,0,0,1,1\"";
	private static String startUpH = "\"0,0,0,0,0,0,0,1\"";
	private static String shutDown = "\"0,0,0,0,0,0,0,0\"";
	

	

		
	public void init() {
		this.powerIp = database.readConfigValue("powerOutletIP");
		this.DC1Ip = database.readConfigValue("DC1IP");
		this.DC2Ip = database.readConfigValue("DC2IP");
		this.DC3Ip = database.readConfigValue("DC3IP");
		this.DC4Ip = database.readConfigValue("DC4IP");
		this.user = database.readConfigValue("dcUser");
		this.pass = database.readConfigValue("dcPassword");
		this.s = database.readConfigValue("SNMP Path");
		this.GET= s+"\\snmpget.exe -v1 -cpublic "+powerIp+" SNMPv2-SMI::enterprises.17420.1.2.9.1.13.0";
		this.SET= s+"\\snmpset.exe -v1 -cpublic "+powerIp+" SNMPv2-SMI::enterprises.17420.1.2.9.1.13.0 s ";
//		this.dC4Power = new IntelATMPowerControl(DC1Ip, bootUser, passDc4);
	}
	
	public Database getDatabase() {
		return database;
	}



	public void setDatabase(Database database) {
		this.database = database;
	}

	private String getStatus() throws Exception{
		
		//Get the current status of the IPower device
		SysCommandExecutor statusCheck = new SysCommandExecutor(); 		
		int exitStatus = statusCheck.runCommand(GET);
		
		//Thread.sleep(4000);
		String statusError = statusCheck.getCommandError();
		String statusOutput = statusCheck.getCommandOutput();
		if(exitStatus!=0){
			log.error("Error executing command"+"\n"+statusError);
			throw new Exception("Error communicating");
		}
		else
			return statusOutput;
	}

	
		
	private String startupSwitch(String port) throws Exception{
		
		SysCommandExecutor startup = new SysCommandExecutor();
		int exitStatus = startup.runCommand(SET+port);
		String startupError = startup.getCommandError();
		String startupOutput = startup.getCommandOutput();
				
		if(exitStatus!=0){
			log.error("Error executing command"+"\n"+startupError);
			throw new Exception("Error communicating");
		}
		else{
			Thread.sleep(7000);
			return startupOutput;	
		}
	}
	
	/*
	 * Function to start up a single switch, written to be used with AJAX implementation of web interface
	 */
	public String startupSwitchSingle(String port, String[] socket) throws Exception{
		String[] curSockets= powerStatus();
		int check=0;
		for (int i =0 ; i<curSockets.length; i++){
			if(Integer.parseInt(curSockets[i])!=Integer.parseInt(socket[i])){
				check=check+1;
			}
		}
		if(check==1){
			log.info("Passing "+port+" to startup");
			return startupSwitch(port);
		}
		if(check==0){
			return "Port already ON";
		}
		else{	
			log.error("More than one socket being switched on at the same time");
			return "Error";
		}
	}
		
	public String startUpSwithchDynamic(int port) throws Exception{
		if(port==0){
			startupSwitch(startUpA);			
		}
		if(port==1){
			startupSwitch(startUpB);			
		}
		if(port==2){
			startupSwitch(startUpC);			
		}
		if(port==3){
			startupSwitch(startUpD);			
		}
		if(port==4){
			startupSwitch(startUpE);			
		}
		if(port==5){
			startupSwitch(startUpF);			
		}
		if(port==6){
			startupSwitch(startUpG);			
		}
		if(port==7){
			startupSwitch(startUpH);			
		}
		else{
			return "Illegal input";
		}
		
		return "Port "+port+" Started";
	}
	
	public String[] powerStatus () throws Exception{
		
		String cmdOutput = getStatus();
		String curStatus = cmdOutput.substring(54,69);
		String delim ="[,]";
		String[] sockets = curStatus.split(delim);
		String  outPut="";
		for (int i =0 ; i<sockets.length; i++)
			outPut =outPut+("Socket "+(i+1)+" is "+sockets[i]+"\n");
		
		return sockets;
	}
	
	public String startUpIPower() throws Exception{
		
		int i=0;
		
		startupSwitch(startUpH);
		i=i+1;
		startupSwitch(startUpG);
		i=i+1;
		startupSwitch(startUpF);
		i=i+1;
		Thread.sleep(2000);
		startupSwitch(startUpE);
		i=i+1;
		startupSwitch(startUpD);
		Thread.sleep(2000);
		i=i+1;
		startupSwitch(startUpC);
		i=i+1;
		startupSwitch(startUpB);
		i=i+1;
		startupSwitch(startUpA);
		i=i+1;
		if(i==8)
			return "Power up complete";
		else
			return"Startup Failed, please check logs";
	}
		
	//Method to be used to start up only a single OP on the IPower board. 
	public void startUpIPower(String socket) throws Exception{
		
		String port = "StartUp"+socket;
		log.debug(port);
		log.debug("This is still not implemented");
		//startupSwitch(port);
		
	}
	
	public String shutDown() throws Exception{
		
		return startupSwitch(shutDown);
	}
	
	public String shutdownDC(String action, String num) throws Exception{
		
		
		if(action =="restart"){
			if (num.equals("1")){
				String OP=DCReStart(DC1Ip);
				return OP;
			}
			if (num.equals("2")){
				String OP=DCReStart(DC2Ip);
				return OP;
			}
			if (num.equals("3")){
				String OP=DCReStart(DC3Ip);
				return OP;
			}
			if (num.equals("4")){
				String OP=DCReStart(DC4Ip);
				return OP;
			}
			if (num.equals("All")){
				String OP=DCReStart(DC1Ip+","+DC2Ip+","+DC3Ip+","+DC4Ip);
				return(OP);
			}
			else{
				return "Illegal comand";
			}
		}
		if(action =="shutdown"){
			if (num.equals("1")){
				String OP=DCShutDown(DC1Ip);
				return(OP);
			}
			if (num.equals("2")){
				String OP=DCShutDown(DC2Ip);
				return(OP);
			}
			if (num.equals("3")){
				String OP=DCShutDown(DC3Ip);
				return(OP);
			}
			if (num.equals("4")){
				String OP=DCShutDown(DC4Ip);
				return(OP);
			}
			if (num.equals("All")){
				String OP=DCShutDown(DC1Ip+","+DC2Ip+","+DC3Ip+","+DC4Ip);
				return(OP);
			}
			else{
				return "Illegal comand";
			}
		}
		else{
		return "Error executing comand";
		}
		
	}
	
	private String DCReStart(String DCip) throws Exception{
		
		String path= this.path+"\\Utils\\";
		String cmd = path+"psshutdown -r -t 05 -n 5 \\\\"+DCip+" -u "+user+" -p "+pass;
		SysCommandExecutor shutdown = new SysCommandExecutor(); 		
		int exitStatus = shutdown.runCommand(cmd);
		String statusError = shutdown.getCommandError();
		String statusOutput = shutdown.getCommandOutput();
		if(exitStatus!=0){
			log.error("Error (" + statusError + ") executing command "+cmd);
			throw new Exception("Error communicating");
			}
		else
			return statusOutput;
		
	}
	private String DCShutDown(String DCip) {
		
		String path= this.path+"\\Utils\\";
		String cmd = path+"psshutdown -k -t 05 -n 3 \\\\"+DCip+" -u "+user+" -p "+pass;
		log.debug(cmd);
		SysCommandExecutor shutdown = new SysCommandExecutor(); 		
		int exitStatus = -1;
		try {
			exitStatus = shutdown.runCommand(cmd);
		} catch (Exception e) {
			log.error("Error shutting down "+DCip+"executing command ");
		}
		String statusOutput = shutdown.getCommandOutput();
		if(exitStatus!=0){
			log.error("Error executing command "+cmd);
			return("Error communicating");
		}
		else
			return statusOutput;
	}
	
	public String dCStartUp() throws Exception{
		boolean response1 = false;
		boolean response2 = false;
		boolean response3 = false;
		boolean response4 = false;
		
		
			IntelATMPowerControl dC1Power = new IntelATMPowerControl(DC1Ip, bootUser, passDc1 );
			response1 = dC1Power.powerUp();
			if(response1){
				log.debug("DC1 Powered on");
			}
			IntelATMPowerControl dC2Power = new IntelATMPowerControl(DC2Ip, bootUser, passDc2 );
			response2 = dC2Power.powerUp();
			if(response2){
				log.debug("DC2 Powered on");
			}
			IntelATMPowerControl dC3Power = new IntelATMPowerControl(DC3Ip, bootUser, passDc3 );
			response3 = dC3Power.powerUp();
			if(response3){
				log.debug("DC3 Powered on");
			}
			IntelATMPowerControl dC4Power = new IntelATMPowerControl(DC4Ip, bootUser, passDc4 );
			response4 = dC4Power.powerUp();
			if(response4){
				log.debug("DC4 Powered on");
			}
			JSONObject reply = new JSONObject();
			reply.put("DC1",String.valueOf(response1));
			reply.put("DC2",String.valueOf(response2));
			reply.put("DC3",String.valueOf(response3));
			reply.put("DC4",String.valueOf(response4));

			return reply.toString();

	}
	
	public String dCPowerCheck() throws Exception{
		String response1 = "";
		String response2 = "";
		String response3 = "";
		String response4 = "";
		
		
			IntelATMPowerControl dC1Power = new IntelATMPowerControl(DC1Ip, bootUser, passDc1 );
			response1 = dC1Power.getPowerState();	
			log.debug("DC1 power state is: "+response1);
			
			IntelATMPowerControl dC2Power = new IntelATMPowerControl(DC2Ip, bootUser, passDc2 );
			response2 = dC2Power.getPowerState();
			log.debug("DC2 power state is: "+response2);
			
			IntelATMPowerControl dC3Power = new IntelATMPowerControl(DC3Ip, bootUser, passDc3 );
			response3 = dC3Power.getPowerState();
			log.debug("DC3 power state is: "+response3);
			
			IntelATMPowerControl dC4Power = new IntelATMPowerControl(DC4Ip, bootUser, passDc4 );
			response4 = dC4Power.getPowerState();
			log.debug("DC4 power state is: "+response4);
			
			JSONObject reply = new JSONObject();
			reply.put("DC1",String.valueOf(response1));
			reply.put("DC2",String.valueOf(response2));
			reply.put("DC3",String.valueOf(response3));
			reply.put("DC4",String.valueOf(response4));

			return reply.toString();

	}
	
	public String getPowerIp() {
		return powerIp;
	}
	

}
