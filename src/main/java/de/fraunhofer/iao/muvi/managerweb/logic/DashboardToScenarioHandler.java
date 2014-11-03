package de.fraunhofer.iao.muvi.managerweb.logic;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DashboardToScenarioHandler {
	
	
	public DashboardToScenarioHandler(File xmlFile){
		
		try {
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			NodeList visualizations = doc.getElementsByTagName("visualization");
			NodeList data = doc.getElementsByTagName("dataset");

			for (int temp = 0; temp < visualizations.getLength(); temp++) {
		 
				Node nNode = visualizations.item(temp);
		 
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
					NamedNodeMap attributes= eElement.getAttributes();
					System.out.println("category : "+attributes.getNamedItem("category").getTextContent());
					System.out.println("dataset : " + attributes.getNamedItem("dataset").getTextContent());
					System.out.println("title : " + eElement.getElementsByTagName("title").item(0).getTextContent());
					if(eElement.getElementsByTagName("description").item(0)!=null){
						System.out.println("description : " +eElement.getElementsByTagName("description").item(0).getTextContent());
					}
					
					
				}
			}
			
			for (int temp = 0; temp < data.getLength(); temp++) {
				 
				Node nNode = data.item(temp);
		 
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {	
		 
					Element eElement = (Element) nNode;
					NamedNodeMap attributes= eElement.getAttributes();
					System.out.println("datasource : "+attributes.getNamedItem("datasource").getTextContent());
					System.out.println("format : " + attributes.getNamedItem("format").getTextContent());
					System.out.println("id : " + attributes.getNamedItem("id").getTextContent());
					if(eElement.getElementsByTagName("parameter").item(0)!=null){
						System.out.println("parameter : " +eElement.getElementsByTagName("parameter").item(0).getTextContent());
					}
					
					
				}
			}
		    } catch (Exception e) {
			e.printStackTrace();
		    }
	}
	

}
