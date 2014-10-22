/**
 * 
 */
package de.fraunhofer.iao.muvi.managerweb.web;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import de.fraunhofer.iao.muvi.managerweb.domain.Image;
import de.fraunhofer.iao.muvi.managerweb.domain.LargeImage;
import de.fraunhofer.iao.muvi.managerweb.domain.Rectangle;
import de.fraunhofer.iao.muvi.managerweb.domain.Scale;
import de.fraunhofer.iao.muvi.managerweb.domain.Scenario;
import de.fraunhofer.iao.muvi.managerweb.domain.Scene;
import de.fraunhofer.iao.muvi.managerweb.domain.Screen;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.logic.ScreenIDCalculator;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

@Controller
public class ImageController extends MainController {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(ImageController.class);

	private int paramScenarioId;
	private int paramSceneNumber;
	private String paramXml;
	private String paramUrl;
	private int paramStartID;
	private int paramWidth;
	private int paramHeight;
	private int paramScreenID;
	private String paramScale;
	private Scale[] scaleValues = Scale.values();
	private URL imageURL = null;
	private String paramImageBackground;
	private String paramScreensList;
	private int largeImageNumber;
	private int imageNumber;

	private boolean createDefaultImage(HttpServletRequest request,
			ModelMap model) {
		paramScenarioId = Integer.parseInt(request.getParameter("scenarioId"));
		paramSceneNumber = Integer
				.parseInt(request.getParameter("sceneNumber"));
		// set all values to default first
		paramXml = "";
		paramUrl = "";
		paramStartID = getFirstEmptyScreenID(paramScenarioId, paramSceneNumber);
		paramWidth = 1;
		paramHeight = 1;
		paramScreenID = getFirstEmptyScreenID(paramScenarioId, paramSceneNumber);
		paramImageBackground = "0,0,0";
		paramScreensList = paramStartID + "";
		setScreensUsed(paramScenarioId, paramSceneNumber, model);
		// POST
		if (isPost(request)) {
			setAction(request, model);
			paramUrl = request.getParameter("imageUrl");
			paramScreensList = request.getParameter("screenSelectorScreenList");
			if (Utils.isEmpty(paramUrl)) {
				error("URL can not be empty", model);
				return false;
			}
			try {
				imageURL = new URL(paramUrl);
			} catch (MalformedURLException e) {
				error("URL is not valid", model);
				return false;
			}
		}
		return true;
	}
	
	private void editLargeImagePageHelper(HttpServletRequest request, ModelMap model) {
		//Populate the page with the data already saved in the DB
		
		paramScenarioId = Integer.parseInt(request.getParameter("scenarioId"));
		paramSceneNumber = Integer.parseInt(request.getParameter("sceneNumber"));
		Scenario scenario = database.getScenario(paramScenarioId);
		Scene scene = scenario.getSceneById(paramSceneNumber-1);
		if(Utils.isNotEmpty(request.getParameter("largeImageNumber"))){
			largeImageNumber = Integer.parseInt(request.getParameter("largeImageNumber"));
			}
		else{
			error("Large Image number not found", model);
			throw new IllegalArgumentException("Large Image number not found");
		}
		LargeImage largeImage = scene.getLargeimages().get(largeImageNumber);
		// set all values to currently saved values
		paramXml = Utils.getXml(largeImage);
		paramUrl = largeImage.getUrl().toString();
		paramStartID = largeImage.getDisplayarea().getRectangle().getStart().getId();
		paramWidth =largeImage.getDisplayarea().getRectangle().getWidth();
		paramHeight = largeImage.getDisplayarea().getRectangle().getHeight();	
		//Retrieve RGB values of background
		String bankground = largeImage.getBackground();
		String delims = "[()]+";
		String[] tokens = bankground.split(delims);
		paramImageBackground = tokens[1];
		//Get the list of screens that are currently used
		List<ScreenID> list = ScreenIDCalculator.getScreenIDList(new Rectangle(new ScreenID(paramStartID), paramHeight, paramWidth));
		model.addAttribute("screenSelectorScreenList", Utils.convertToString(list));
		
		setScreensUsed(paramScenarioId, paramSceneNumber, model);
		//Update the request
		setAction(request, model);
	}
	
	private void editImagePageHelper(HttpServletRequest request, ModelMap model) {
		//Populate the page with the data already saved in the DB
		
		paramScenarioId = Integer.parseInt(request.getParameter("scenarioId"));
		paramSceneNumber = Integer.parseInt(request.getParameter("sceneNumber"));
		Scenario scenario = database.getScenario(paramScenarioId);
		Scene scene = scenario.getSceneById(paramSceneNumber-1);
		if(Utils.isNotEmpty(request.getParameter("imageNumber"))){
			imageNumber = Integer.parseInt(request.getParameter("imageNumber"));
			}
		else{
			error("Image number not found", model);
			throw new IllegalArgumentException("Image number not found");
		}
		Image image = scene.getScreens().get(imageNumber).getImage();
		// set all values to currently saved values
		paramXml = Utils.getXml(image);
		paramUrl = image.getUrl().toString();
		paramScreenID = scene.getScreens().get(imageNumber).getId().getId();
		paramScale = image.getScale().name();
		//Retrieve RGB values of background
		//Get the list of screens that are currently used
		model.addAttribute("screenSelectorScreenList", paramScreenID);
		setScreensUsed(paramScenarioId, paramSceneNumber, model);
		//Update the request
		setAction(request, model);
	}


	@RequestMapping(value = "newLargeImage.do")
	public String newLargeImage(HttpServletRequest request, ModelMap model) {
	String editMode= "";
	if(Utils.isNotEmpty(request.getParameter("editMode"))){
		editMode = request.getParameter("editMode");
		}
	if(!editMode.contains("edit")){
		if (createDefaultImage(request, model)) {
			if (isPost(request)) {
				paramScreensList = request.getParameter("screenSelectorScreenList");
				
				Rectangle rectangle = ScreenIDCalculator.tryToGetRectangleFromListOfScreenIDs(Utils.convertToScreenIDList(paramScreensList));
				
				paramStartID = rectangle.getStart().getId();
				paramWidth = rectangle.getWidth();
				paramHeight = rectangle.getHeight();
				

				LargeImage image = new LargeImage(imageURL, new ScreenID(paramStartID), paramWidth, paramHeight);

				if (Utils.isNotEmpty(request
						.getParameter("paramImageBackground"))) {
					paramImageBackground = request
							.getParameter("paramImageBackground");
					image.setBackground("rgb(" + paramImageBackground + ");");
				}
				
				paramScreensList = paramStartID + "";

				if (Utils.isNotEmpty(request.getParameter("paramImageHeight"))
						&& Utils.isNotEmpty(request
								.getParameter("paramImageWidth"))) {
					// delegate values to view before it can crash
					model.addAttribute("paramImageHeight",
							request.getParameter("paramImageHeight"));
					model.addAttribute("paramImageWidth",
							request.getParameter("paramImageWidth"));
					try {
						Integer paramImageHeight = Integer.parseInt(request
								.getParameter("paramImageHeight"));
						Integer paramImageWidth = Integer.parseInt(request
								.getParameter("paramImageWidth"));
						image.setHeight(paramImageHeight);
						image.setWidth(paramImageWidth);
					} catch (NumberFormatException e) {
						error("Height and width values must be numeric!", model);
						setParamsForView(model);
						return "newLargeImage.jsp";
					}
				}
				paramXml = Utils.getXml(image);
								
				if (ACTION_SAVE.equals(action)) {
					return addLargeImageAndSave(image);
				}
			}
			List<ScreenID> list = ScreenIDCalculator.getScreenIDList(new Rectangle(new ScreenID(paramStartID), paramHeight, paramWidth));
			model.addAttribute("screenSelectorScreenList", Utils.convertToString(list));
			
			setParamsForView(model);
		}
			
	}
		//If the request was an edit request
		// First populate the page to show previously saved data.
	else{
		try{
			editLargeImagePageHelper(request,model);
			setParamsForEditView(model);
			//If there was a save request inside an edit image page
			if (isPost(request)) {
				if (ACTION_SAVE.equals(action)) {
					paramUrl = request.getParameter("imageUrl");
					if (Utils.isEmpty(paramUrl)) {
						error("URL can not be empty", model);
						model.addAttribute("editMode", editMode);
						setParamsForEditView(model);
						return "newLargeImage.jsp";
					}
					try {
						imageURL = new URL(paramUrl);
					} catch (MalformedURLException e) {
						error("URL is not valid", model);
						model.addAttribute("editMode", editMode);
						setParamsForEditView(model);
						return "newLargeImage.jsp";
					}
					paramScreensList = request.getParameter("screenSelectorScreenList");
					Rectangle rectangle = ScreenIDCalculator.tryToGetRectangleFromListOfScreenIDs(Utils.convertToScreenIDList(paramScreensList));
					paramStartID = rectangle.getStart().getId();
					paramWidth = rectangle.getWidth();
					paramHeight = rectangle.getHeight();
					LargeImage image = new LargeImage(imageURL, new ScreenID(paramStartID), paramWidth, paramHeight);
					if (Utils.isNotEmpty(request
							.getParameter("paramImageBackground"))) {
						paramImageBackground = request
								.getParameter("paramImageBackground");
						image.setBackground("rgb(" + paramImageBackground + ");");
					}
					Integer largeImageNumber = Integer.parseInt(request.getParameter("largeImageNumber"));
					paramXml = Utils.getXml(image);
					
					return editCurrentLargeImage(image, largeImageNumber);
				}
			}
		}
		catch(Exception e) {
				return "showScenario.do?id="+paramScenarioId;
		}
	}
		return "newLargeImage.jsp";
}

	@RequestMapping(value = "newImage.do")
	public String newImage(HttpServletRequest request, ModelMap model) {
		String editMode= "";
		if(Utils.isNotEmpty(request.getParameter("editMode"))){
			editMode = request.getParameter("editMode");
			}
		if(!editMode.contains("edit")){
			if (createDefaultImage(request, model)) {
				paramScreenID = getFirstEmptyScreenID(paramScenarioId,
						paramSceneNumber);
				if (isPost(request)) {
					paramScreenID = Integer.parseInt(request
							.getParameter("paramScreenID"));
					paramScale = request.getParameter("imageScale");
					Scale scale = Scale.valueOf(paramScale);
					Image image = new Image(imageURL, scale);
					Screen screen = new Screen();
					screen.setId(Utils.convertToScreenIDList(paramScreensList).get(0));
					screen.setImage(image);
					paramXml = Utils.getXml(screen);
					model.addAttribute("screenSelectorScreenList", paramScreenID);
					if (ACTION_SAVE.equals(action)) {
						return addScreenAndSave(paramScenarioId, paramSceneNumber,
								screen);
					}
				}
			}
			setParamsForView(model);
			model.addAttribute("screenSelectorScreenList", paramScreenID);
		}
		else{
			try{
				editImagePageHelper(request,model);
				setParamsForEditView(model);
				if (isPost(request)) {
					if (ACTION_SAVE.equals(action)) {
						paramScreenID = Integer.parseInt(request
								.getParameter("paramScreenID"));
						paramScale = request.getParameter("imageScale");
						Scale scale = Scale.valueOf(paramScale);
						paramUrl = request.getParameter("imageUrl");
						if (Utils.isEmpty(paramUrl)) {
							error("URL can not be empty", model);
							model.addAttribute("editMode", editMode);
							setParamsForEditView(model);
							return "newLargeImage.jsp";
						}
						try {
							imageURL = new URL(paramUrl);
						} catch (MalformedURLException e) {
							error("URL is not valid", model);
							model.addAttribute("editMode", editMode);
							setParamsForEditView(model);
							return "newLargeImage.jsp";
						}
						Image image = new Image(imageURL, scale);
						Screen screen = new Screen();
						paramScreensList = request.getParameter("screenSelectorScreenList");
						screen.setId(Utils.convertToScreenIDList(paramScreensList).get(0));
						screen.setImage(image);
						paramXml = Utils.getXml(screen);
						model.addAttribute("screenSelectorScreenList", paramScreenID);
						return editScreenAndSave(paramScenarioId, paramSceneNumber,screen, imageNumber);
					}
					
				}
			}
			catch (Exception e){
				return "showScenario.do?id="+paramScenarioId;
			}
			
		}
		return "newImage.jsp";
	}

	private void setParamsForView(ModelMap model) {
		// common params
		model.addAttribute("paramScenarioId", paramScenarioId);
		model.addAttribute("paramSceneNumber", paramSceneNumber);
		model.addAttribute("paramXml", paramXml);
		model.addAttribute("paramUrl", paramUrl);
		model.addAttribute("scaleValues", scaleValues);
		model.addAttribute("paramColors", paramColors);
		// large image
		model.addAttribute("paramStartID", paramStartID);
		model.addAttribute("paramWidth", paramWidth);
		model.addAttribute("paramHeight", paramHeight);
		// will be set in the function newLargeImage
		// model.addAttribute("paramImageHeight", paramImageHeight);
		// model.addAttribute("paramImageWidth", paramImageWidth);
		model.addAttribute("paramImageBackground", paramImageBackground);
		// image
		model.addAttribute("paramScreenID", paramScreenID);
		model.addAttribute("paramScale", paramScale);
	}
	
	private void setParamsForEditView(ModelMap model) {
		// common params
		model.addAttribute("paramScenarioId", paramScenarioId);
		model.addAttribute("paramSceneNumber", paramSceneNumber);
		model.addAttribute("paramXml", paramXml);
		model.addAttribute("paramUrl", paramUrl);
		model.addAttribute("scaleValues", scaleValues);
		model.addAttribute("paramColors", paramColors);
		// large image
		model.addAttribute("paramStartID", paramStartID);
		model.addAttribute("paramWidth", paramWidth);
		model.addAttribute("paramHeight", paramHeight);
		model.addAttribute("largeImageNumber", largeImageNumber);

		// will be set in the function newLargeImage
		// model.addAttribute("paramImageHeight", paramImageHeight);
		// model.addAttribute("paramImageWidth", paramImageWidth);
		model.addAttribute("paramImageBackground", paramImageBackground);
		// image
		model.addAttribute("paramScreenID", paramScreenID);
		model.addAttribute("paramScale", paramScale);
		model.addAttribute("imageNumber", imageNumber);
	}

	private String addLargeImageAndSave(LargeImage image) {
		Scenario scenario = database.getScenario(paramScenarioId);
		Scene scene = scenario.getScenes().get(paramSceneNumber - 1);
		List<LargeImage> iList = scene.getLargeimages();
		iList.add(image);
		scene.setLargeimages(iList);
		database.saveOrUpdateScenario(scenario);
		return "showScenario.do?id=" + scenario.getId();
	}
	
	private String editCurrentLargeImage(LargeImage image, int imageIndex) {
		Scenario scenario = database.getScenario(paramScenarioId);
		Scene scene = scenario.getScenes().get(paramSceneNumber - 1);
		List<LargeImage> iList = scene.getLargeimages();
		iList.remove(imageIndex);
		iList.add(imageIndex, image);
		scene.setLargeimages(iList);
		database.saveOrUpdateScenario(scenario);
		return "showScenario.do?id=" + scenario.getId();
	}

}
