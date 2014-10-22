package de.fraunhofer.iao.muvi.managerweb.logic;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.fraunhofer.iao.muvi.managerweb.backend.Database;
import de.fraunhofer.iao.muvi.managerweb.domain.DisplayArea;
import de.fraunhofer.iao.muvi.managerweb.domain.Image;
import de.fraunhofer.iao.muvi.managerweb.domain.LargeImage;
import de.fraunhofer.iao.muvi.managerweb.domain.Rectangle;
import de.fraunhofer.iao.muvi.managerweb.domain.Scale;
import de.fraunhofer.iao.muvi.managerweb.domain.Scenario;
import de.fraunhofer.iao.muvi.managerweb.domain.Scene;
import de.fraunhofer.iao.muvi.managerweb.domain.Screen;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;


public class PresentationHandler {

	private static final Log log = LogFactory.getLog(PresentationHandler.class);
	private Database database;
	private String originalNname ="";
	private String path ="";
	private int height = 0;
	private int width = 0;
	private ArrayList<String> slideNames;
	
	public PresentationHandler (String originalNname, String path, Database database,int height, int width) {
		this.originalNname = originalNname;
		this.path = path;
		this.height = height;
		this.width = width;
		this.database = database;
	}

	
	public String scenarioGenerator (boolean overview, boolean zoom) throws MalformedURLException {
		/*Helper to create the desired scenario*/
		Scenario scenario = new Scenario();
		scenario.setName(scenarioName());
		scenario.setDate(new Date());
		scenario.addTag("ppt");
		scenario.addTag(originalNname);
		scenario.setTags(scenario.getTags());
		scenario.setDescription("Auto generated Scenario from PowerPoint presentation " + scenario.getName());
		/*Generate the desired scenes based on options selected*/
		List<Scene> scenes = new ArrayList<Scene>();
		if(overview){
			scenes.addAll(overviewScenes());
		}
		if(zoom){
			scenes.addAll(slideToScenes());
		}
		
		scenario.setScenes(scenes);
		database.saveOrUpdateScenario(scenario);
		log.debug("Scenario constructed successfully, new scenario name is "+scenarioName());
		return "../manager/playScenario.do?id="+scenario.getId();
	}

	public List<Scene> overviewScenes() throws MalformedURLException {		
		List<Scene> scenes = new ArrayList<Scene>();	
		int overviewSets = (slideNames.size()-1)/36;	
		int slidesPerOverview = (int) Math.ceil(slideNames.size()/(overviewSets+1.f));
		int rowsPerOverview = slidesPerOverview/6;
		int rowsToCenter = 6-rowsPerOverview;
		int startDisplay = ((rowsToCenter/2)*6)+1;
		for (int i=0; i<=overviewSets; i++){
			scenes.add(new Scene());
			scenes.get(i).setName("Overview "+(i+1));
			scenes.get(i).setScreens(new ArrayList<Screen>());
			scenes.get(i).setResetColor("rgb(0,0,0);");
		}
		int n=0;
		//Create the overview Scene (1 image per screen)
		for(String name : slideNames){
			List<Screen> screens =  scenes.get(n/slidesPerOverview).getScreens();
			
			Screen screen = new Screen();
			screen.setId(new ScreenID((n%slidesPerOverview)+startDisplay));
			screen.setImage(new Image(new URL (database.readConfigValue("managerURL") + "apps/readImage.jsp?img=file:///"
					+path+"\\"+name),
					Scale.original));
			screens.add(screen);
			n=n+1;			
		}
		return scenes;
	}
	
	public List<Scene> slideToScenes () throws MalformedURLException {
		List<Scene> scenes = new ArrayList<Scene>();	
		/*iterate over all images in the folder and make a large Image of each*/
		int n =1;
		for(String name : slideNames){
			/*Create a scene and name it*/
			Scene scene = new Scene();
			scene.setName("Slide "+n);
			/*Create a List of LargeImage class to hold the large Image*/
			ArrayList<LargeImage> largeImages = new ArrayList<LargeImage>();
			/*Create a largeImage of the Slide*/
			LargeImage bigSlide = new LargeImage();
			bigSlide.setBackground("rgb(0,0,0);");
			bigSlide.setHeight(height);
			bigSlide.setWidth(width);
			bigSlide.setUrl(new URL (database.readConfigValue("managerURL") + "apps/readImage.jsp?img=file:///"
					+path+"\\"+name));
			bigSlide.setDisplayarea(new DisplayArea(new Rectangle(new ScreenID(1), 6, 6)));	
			largeImages.add(bigSlide);
			scene.setLargeimages(largeImages);
			scene.setScreenshot(new URL (database.readConfigValue("managerURL") + "apps/readImage.jsp?img=file:///"
					+path+"\\"+name));
			scenes.add(scene);
			n++;
		}

		
		return scenes;
	}
	
	public List<URL> mapImageUrls () throws MalformedURLException {
		ArrayList<URL> imageUrls = new ArrayList<URL>();
		for(String name : slideNames){
			imageUrls.add(new URL (database.readConfigValue("managerURL") + "apps/readImage.jsp?img=file:///"
					+path+"\\"+name));	
		}
		return imageUrls;
	}
	
	public String scenarioName() {
		String[] tokens = path.split("\\\\");
		return tokens[tokens.length-1];
	}
	
	private static String getNewName(String oldName) {
		oldName = oldName.replaceAll("Folie", "");
		if (oldName.length() < 2 + 4) {
			oldName = "0" + oldName;
		}
		if (oldName.length() < 3 + 4) {
			oldName = "0" + oldName;
		}
		return oldName;
	}
	
	public void listAndRenameFilesForFolder(final File folder) throws Exception {
		ArrayList<String> fileNames = new ArrayList<String>();
		try {
			 for (final File fileEntry : folder.listFiles()) {
			        if (!fileEntry.isDirectory()) {
			        	/* Rename files... */
			        	try {
			        		String patent = fileEntry.getParent()+"\\";
			        		String newName = getNewName(fileEntry.getName());
							Files.copy(fileEntry.toPath(), Paths.get(patent+newName),StandardCopyOption.REPLACE_EXISTING);
							Files.delete(fileEntry.toPath());
				            fileNames.add(newName);
						} catch (Exception e) {
							log.error(e.getMessage());
							throw new Exception ("Unable to rename files");
							
						}	        	

			        }
			    }
		} catch (NullPointerException e) {
			throw new Exception ("Illigal file name");
		}
	   
	    /*Sort the list to process it in order*/
	    Collections.sort(fileNames);
	    this.slideNames = fileNames;
	}
	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

}
