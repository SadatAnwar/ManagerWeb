package de.fraunhofer.iao.muvi.managerweb.logic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PowerPointConverter extends Thread {
	private Runtime runtime;
	private Process pptConverter;
	public Stack<String> output = new Stack<String>();
	private static final String PPT_CONVERTER_EXE = "C:/CWM/PresentationController/Fraunhofer.CloudWall.PPTConverter.exe";
	private static final Log log = LogFactory.getLog(PowerPointConverter.class);

	
	public PowerPointConverter() {
		runtime= Runtime.getRuntime();
	}
	
	@Override
	public void run() {
		try {
			pptConverter = runtime.exec(PPT_CONVERTER_EXE);
			
			readOutput();

			if(pptConverter.waitFor() == 0){
				log.debug("PPT conversion successful");
			} else {
				pptConverter.destroy();
				log.error("PPT to image conversion took too long, operation killed");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	public void readOutput() {
		try{
			InputStream stderr = pptConverter.getInputStream();
			InputStreamReader isr = new InputStreamReader(stderr,"ISO-8859-1");
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            while ( (line = br.readLine()) != null) {
            	output.add(line);	   
            }
		} catch (Exception e){
			log.error(e.getMessage(), e);	
		}
	}
	public void close() {
		pptConverter.destroy();
	}
}