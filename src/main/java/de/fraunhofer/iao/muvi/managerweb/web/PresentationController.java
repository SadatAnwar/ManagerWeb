package de.fraunhofer.iao.muvi.managerweb.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import de.fraunhofer.iao.muvi.managerweb.logic.PowerPointConverter;
import de.fraunhofer.iao.muvi.managerweb.logic.PresentationHandler;

public class PresentationController extends MainController {
	
	private static final Log log = LogFactory.getLog(ScenariosController.class);
	private static final String PPT_DIRECTORY_BASE = "C:/CWM/PowerPoints/ppts/";
	private static final String IMAGE_DIRECTORY_BASE = "C:/CWM/PowerPoints/";

	@RequestMapping(value = "presentationUpload.do")
	public String uploadImage(HttpServletRequest request, ModelMap model) throws Exception {

		List<URL> newImagesURLs = null;
		HashMap<String, String> parameters = new HashMap<String, String>();
		String fileName="";
		FileItemIterator iter = new ServletFileUpload(new DiskFileItemFactory()).getItemIterator(request);
		InputStream fileInputStream = null;
		int height = 0;
		int width = 0;
		while (iter.hasNext()) {
			FileItemStream item = iter.next();
			
			if(item.isFormField()) {
			  
				// Extract parameters from web-page
			   String name = item.getFieldName();
			   String value = Streams.asString(item.openStream());
			   parameters.put(name, value);
			} else {
				
				//This is a file item, make sure its a power-point presentation
				if(item.getContentType().contains("presentation")||item.getContentType().contains("powerpoint")){
					
					//Save the file in memory to process later
					fileName = item.getName();
					InputStream is = item.openStream();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int len;
					while ((len = is.read(buffer)) > -1 ) {
					    baos.write(buffer, 0, len);
					}
					baos.flush();
					fileInputStream = new ByteArrayInputStream(baos.toByteArray());
					baos.close();
				} else {
					
					//Invalid file type uploaded log and inform user
					model.addAttribute("error", "Invalid file uploaded, please make sure file is of presentation type");
					log.error("Invalid file uploaded");
					fileInputStream.close();
					return "presentationUpload.jsp"; 
				}
			}
		}
		
		/*Set aspect ratio for images*/
		String aspectRatio = parameters.get("aspectRatio");
		if(aspectRatio.contains("640")) {
			width = 640;
			height = 480;
		} else {
			width = 1920;
			height = 1080;
		}
		String newFileName = null;
		
		if(fileInputStream !=null) {
			
			//Start the ppt converter utility
			PowerPointConverter pptConverter = new PowerPointConverter();
			String originalNname = fileName.substring(0, fileName.lastIndexOf("."));
			String newName = originalNname+"_"+aspectRatio+fileName.substring(fileName.lastIndexOf("."));
			pptConverter.start();
			try {
				//Wait so the C# utility can get ready to receive the file
				Thread.sleep(2500);
				//Save file 
				newFileName =PPT_DIRECTORY_BASE+newName;
				File newPresentationFile = new File(newFileName);
				if(!newPresentationFile.exists()) {
					/*Do only if the file doesnot exist*/
					OutputStream outputStream  =  new FileOutputStream(newPresentationFile);
					byte[] bytes = new byte[1024];
					int read = 0;
					while ((read = fileInputStream.read(bytes)) != -1) {
						outputStream.write(bytes, 0, read);
					}
					outputStream.close();
					fileInputStream.close();
					log.debug("Saved presentation to " + newFileName+" now generating images");
					pptConverter.join();
					log.debug("Images generation complete, building scenario");
					String line;
					String[] tokens=null;
					while(!pptConverter.output.isEmpty()){
						line = pptConverter.output.pop();
						if(line.contains("Export directory")) {
							tokens = line.split("[=>]");
							String path = tokens[1];
							log.debug("Images generated at: "+path);
							PresentationHandler presentationHandler = new PresentationHandler(originalNname, path, database, height, width);
							presentationHandler.listAndRenameFilesForFolder(new File(path));
							
							String scenarioUrl= presentationHandler.scenarioGenerator(parameters.containsKey("generateOverview"),
																	parameters.containsKey("generateZoom"));
							String scenatioLink = "<a href=\""+scenarioUrl+"\">"+presentationHandler.scenarioName()+"</a>";
							model.addAttribute("message", "Scenario constructed successfully, new scenario name is" +scenatioLink);
							newImagesURLs = presentationHandler.mapImageUrls();
							model.addAttribute("imagesURLs", newImagesURLs);
						}
					}
				} else {
					/*if file is already present, alert user and kill the c# util*/
					pptConverter.close();
					pptConverter.join(10000); //Just to be sure the util was killed surely
					log.error("Duplicate file uploaded, no further processing will be done");
					model.addAttribute("error", "You just uploaded a duplicate file, check if a relevant scenario already exists, "
							+ "if you have to upload this file any way, please delete the current version from "+newFileName.replaceAll("C:/CWM/", "Z:/")
							+ ", also be sure to delete the folder containing images located at "+IMAGE_DIRECTORY_BASE.replaceAll("C:/CWM/", "Z:/")+newName);
				}
			} catch (Exception e) {
				model.addAttribute("error", "An error happened: "+e.getMessage());
				log.error(e.getMessage(), e);
			}
		}
		return "presentationUpload.jsp";
	}
	
}
