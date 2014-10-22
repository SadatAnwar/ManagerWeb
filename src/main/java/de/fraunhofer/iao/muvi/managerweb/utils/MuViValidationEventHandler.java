package de.fraunhofer.iao.muvi.managerweb.utils;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

public class MuViValidationEventHandler implements ValidationEventHandler {
	
	private String message;
	
	MuViValidationEventHandler(String message) {
		this.message = message;
	}

	public boolean handleEvent(ValidationEvent event) {
		
		this.message = event.getMessage() + " (line " + event.getLocator().getLineNumber() + ", column " + event.getLocator().getColumnNumber() + ")";
		
//        System.out.println("\nEVENT");
//        System.out.println("SEVERITY:  " + event.getSeverity());
//        System.out.println("MESSAGE:  " + event.getMessage());
//        System.out.println("LINKED EXCEPTION:  " + event.getLinkedException());
//        System.out.println("LOCATOR");
//        System.out.println("    LINE NUMBER:  " + event.getLocator().getLineNumber());
//        System.out.println("    COLUMN NUMBER:  " + event.getLocator().getColumnNumber());
//        System.out.println("    OFFSET:  " + event.getLocator().getOffset());
//        System.out.println("    OBJECT:  " + event.getLocator().getObject());
//        System.out.println("    NODE:  " + event.getLocator().getNode());
//        System.out.println("    URL:  " + event.getLocator().getURL());
        return true;
    }
	
	public String getMessage() {
		return message;
	}
	
}
