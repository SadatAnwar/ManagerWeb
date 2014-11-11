package de.fraunhofer.iao.muvi.managerweb.web;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import de.fraunhofer.iao.muvi.managerweb.domain.Scenario;
import de.fraunhofer.iao.muvi.managerweb.domain.Scene;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

@Controller
public class ScenariosController extends MainController {

	private static final Log log = LogFactory.getLog(ScenariosController.class);
	
	private static final String IMAGE_DIRECTORY_BASE = "Z:/images/upload/";
	
	   // see this How-to for a faster way to convert
	   // a byte array to a HEX string
	   public static String getMD5Checksum(byte[] b) throws Exception {
	       String result = "";

	       for (int i=0; i < b.length; i++) {
	           result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
	       }
	       return result;
	   }	

	@RequestMapping(value = "uploadImage.do")
	@ResponseBody
	public String uploadImage(HttpServletRequest request, ModelMap model) throws IOException  {
		
		log.debug("Upload new images...");
		List<URL> newImagesURLs = new ArrayList<>();
				
		try {
			@SuppressWarnings("unchecked")
			List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
			if (items != null) {
				for (FileItem item : items) {
					String url = null;
					String newFileName = null;
					if(item.getContentType().startsWith("image")){
						
						MessageDigest md= null;
						try {
							md = MessageDigest.getInstance("MD5");
						} catch (NoSuchAlgorithmException e1) {
							e1.printStackTrace();
						}
						OutputStream outputStream = null;
						try {
							InputStream is = new ByteArrayInputStream(item.get());
						  DigestInputStream dis = new DigestInputStream(is, md);
						     byte[] buffer = new byte[1024];
						        while (dis.read(buffer) > -1);
						  byte[] digest = md.digest();
						  String checksum = getMD5Checksum(digest);
						  //Save file to directory, with name as FILE_NAME_7charectersOfMD5.JPG
						  newFileName =IMAGE_DIRECTORY_BASE+
								  item.getName().subSequence(0, item.getName().indexOf("."))+"_"+
								  checksum.substring(0,7)+".jpg";
						  File newImageFile = new File(newFileName);
						  outputStream =  new FileOutputStream(newImageFile);
						  byte[] bytes = new byte[1024];
						  int read = 0;
						  is = new ByteArrayInputStream(item.get());
							while ((read = is.read(bytes)) != -1) {
								outputStream.write(bytes, 0, read);
							}
							outputStream.close();
							is.close();
							dis.close();
							log.debug("Saved one image to " + newFileName);
							URL newURL = Utils.getURLFromImageFile(newImageFile);
							url  = newURL.toString();
							log.debug(url);
							return url;
						} catch (Exception e) {
							
							log.error(e.getMessage(), e);
						}
						
					}
				}
			}
		} catch (FileUploadException e) {
			log.error(e.getMessage(), e);
		}
		
		model.addAttribute("imagesURLs", newImagesURLs);
		
		return "newImageUpload.jsp";
	}
		
	
	@RequestMapping(value = "readFileList.do")
	public String readFileList(HttpServletRequest request, ModelMap model) throws Exception {
		List<URL> newImagesURLs = new ArrayList<>();
		File folder = new File(IMAGE_DIRECTORY_BASE);
		File[] files = folder.listFiles();
		
		/* Sort files by date, newest first! */
		Arrays.sort(files, new Comparator<File>(){
		    public int compare(File f1, File f2)
		    {
		        return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
		    } });
		
		ArrayList<File> fileList = new ArrayList<File>();
		
		if (files != null && files.length > 0) {
			fileList.addAll(Arrays.asList(files));
		}
		
		URL fileUrl;
		for(int i = 0; i<fileList.size(); i++) {
			fileUrl = Utils.getURLFromImageFile(fileList.get(i));
			newImagesURLs.add(fileUrl);
		}
		model.addAttribute("imagesURLs", newImagesURLs);

		return "uploadImageGallery.jsp";
	}
	
	@RequestMapping(value = "scenarios.do")
	public String loadScenarios(HttpServletRequest request, ModelMap model) {
		// set show mode
		if (Utils.isNotEmpty(request.getParameter("mode"))) {
			request.getSession().setAttribute("mode",
					request.getParameter("mode"));
		}
		
		setAction(request, model);
		if (isPost(request)) {

			if (ACTION_DELETE.equals(action)) {
				Integer id = Integer.parseInt(request.getParameter("id"));
				database.deleteScenario(id);
				String message = "Scenario " + id + " deleted.";
				message(message, model);
				log.debug(message);

			}
			
		} else if (Utils.isNotEmpty(action)) {

			try {
				Integer id = Integer.parseInt(request.getParameter("id"));
				Scenario scenario = database.getScenario(id);
				if (scenario != null) {
					if ("show".equals(action)) {
						showMuVi.showScenario(scenario);
						message("Started to show scenario " + id + ".", model);
					}
				} else {
					throw new IllegalArgumentException("No scenario with ID "
							+ id + " found in database.");
				}
			} catch (Exception e) {
				log.error("Error: " + e.getMessage(), e);
				error("Error: " + e.getMessage(), model);
			}
		} 
			
		List<Scenario> scenarios = new ArrayList<>();

		if (request.getParameter("tag") != null) {
			scenarios = database.getScenarios(request.getParameter("tag"));
			model.addAttribute("tag", request.getParameter("tag"));
		} else {
			scenarios = database.getScenarios();
		}

		log.debug("Retrieved " + scenarios.size() + " scenarios from database.");

		model.addAttribute("scenarios", scenarios);

		List<String> tags = new ArrayList<>();
		for (Scenario scenario : scenarios) {
			if (Utils.isNotEmpty(scenario.getTags())) {
				for (String tag : scenario.getTags()) {
					if (!tags.contains(tag)) {
						tags.add(tag);
					}
				}
			}
		}
		Collections.sort(tags);

		model.addAttribute("tags", tags);


		return "scenarios.jsp";
	}

	@RequestMapping(value = "playScenario.do")
	public String playScenario(HttpServletRequest request, ModelMap model) {

		Integer id = Integer.parseInt(request.getParameter("id"));
		Scenario scenario = database.getScenario(id);
		model.addAttribute("scenario", scenario);

		return "scenarioPlayer.jsp";
	}
	
	/*
	 * Written to play the entire scenario as slide show. 
	 */
	@RequestMapping(value = "playEntireScenario.do")
	@ResponseBody
	public String playEntireScenario(HttpServletRequest request, ModelMap model) throws InterruptedException {
		
		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version='1.0' encoding='UTF-8'?>");
		xml.append("<Scenario>");
		Integer scenarioId = Integer.parseInt(request.getParameter("scenarioId"));
		Scenario scenario = database.getScenario(scenarioId);
		
		
		Integer sceneId = Integer.parseInt(request.getParameter("sceneId"));
		Scene scene = scenario.getSceneById(sceneId);
		
		if(scene.getScreenshot()!=null){
			xml.append("<Sceneshot>").append(scene.getScreenshot()).append("</Sceneshot>");
		}
		else{
			xml.append("<Sceneshot>").append("null").append("</Sceneshot>");
		}
				
		String delayDuration = "30000";
		if(scene.getDuration() != null && scene.getDuration() > 0){
			delayDuration = String.valueOf(1000 * scene.getDuration());
		}
		xml.append("<Duration>").append(delayDuration).append("</Duration>");
		xml.append("</Scenario>");
		
		log.info("Playing scene number "+sceneId);
		
		showMuVi.showScene(scene);

		return xml.toString();
	}

	//Creates a new scenario
	@RequestMapping(value = "newScenario.do")
	public String newScenario(HttpServletRequest request, ModelMap model) {
		if ("POST".equalsIgnoreCase(request.getMethod())) {
			Scenario scenario = new Scenario();
			scenario.setName(request.getParameter("sName"));
			scenario.setDescription(request.getParameter("sDesc"));
			database.saveOrUpdateScenario(scenario);
			return "showScenario.do?id=" + scenario.getId();
		}
		return "newScenario.jsp";
	}

	@RequestMapping(value = "showScenario.do")
	public String editScenario(HttpServletRequest request, ModelMap model) {

		Integer id = Integer.parseInt(request.getParameter("id"));
		Scenario scenario = database.getScenario(id);
		if (scenario != null) {
			model.addAttribute("scenario", scenario);
		}
		return "showScenario.jsp";
	}

	@RequestMapping(value = "showScene.do")
	@ResponseBody
	public String showScene(HttpServletRequest request, ModelMap model) {

		String message = "";

		try {

			Integer scenarioId = Integer.parseInt(request
					.getParameter("scenarioId"));
			Scenario scenario = database.getScenario(scenarioId);
			if (scenario != null) {
				Integer sceneNumber = Integer.parseInt(request
						.getParameter("sceneNumber"));

				if (Utils.isNotEmpty(scenario.getScenes())) {
					if (scenario.getScenes().size() >= sceneNumber
							&& sceneNumber > 0) {
						message = "Showing scene " + sceneNumber
								+ " from scenario " + scenarioId;
						log.info(message);
						showMuVi.showScene(scenario.getScenes().get(
								sceneNumber - 1));
					}
				}

				model.addAttribute("scenario", scenario);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			message = "Error: " + e.getMessage();
		}

		return message;
	}
	
	@RequestMapping(value = "sceneScreenShot.do")
	@ResponseBody
	public String sceneScreenShot(HttpServletRequest request, ModelMap model) {

		String message = "";

		try {

			Integer scenarioId = Integer.parseInt(request
					.getParameter("scenarioId"));
			Scenario scenario = database.getScenario(scenarioId);
			Scene scene = null;
			File image = null;
			if (scenario != null) {
				Integer sceneNumber = Integer.parseInt(request
						.getParameter("sceneNumber"));
				if (Utils.isNotEmpty(scenario.getScenes())) {
					if (scenario.getScenes().size() >= sceneNumber
							&& sceneNumber > 0) {
						scene = scenario.getScenes().get(sceneNumber-1);
						message = "Taking screenshot of scene " + sceneNumber
								+ " from scenario " + scenarioId;
						log.info(message);
						image = MuViScreenshot.getScreenShot("Scenario-"+scenarioId,"scene-"+sceneNumber);
						message = "Taking screenshot complete, file now present in \\\\ScreenShot\\MuVi\\" 
						+scenarioId+"\\"+ sceneNumber;
						log.info(message);
					}
				}
				String readImageUrl = "http://137.251.35.100:3636/manager/apps/readImage.jsp?img=file:///";
				scene.setScreenshot(new URL(readImageUrl+image.getAbsolutePath()));
				database.saveOrUpdateScenario(scenario);
				model.addAttribute("scenario", scenario);
				return scene.getScreenshot().toString();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			message = "Error: " + e.getMessage();
		}

		return message;
	}

}
