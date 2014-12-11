package de.fraunhofer.iao.muvi.managerweb.web;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.apache.commons.logging.Log;
import de.fraunhofer.iao.muvi.managerweb.domain.AnimatedText;
import de.fraunhofer.iao.muvi.managerweb.domain.LargeText;
import de.fraunhofer.iao.muvi.managerweb.domain.Rectangle;
import de.fraunhofer.iao.muvi.managerweb.domain.Scenario;
import de.fraunhofer.iao.muvi.managerweb.domain.Scene;
import de.fraunhofer.iao.muvi.managerweb.domain.Screen;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.domain.Text;
import de.fraunhofer.iao.muvi.managerweb.logic.ScreenIDCalculator;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

/**
 * @author ivanniko
 * 
 */
@Controller
public class TextController extends MainController {

	private int paramScenarioId;
	private int paramSceneNumber;
	private String paramXml;
	private int paramScreenID;
	private String paramText;
	private String paramStyle;
	private String paramStartBgColor;
	private String paramStartTxtColor;
	private Screen screen;
	private int textNumber;
	private int animatedTextNumber;

	/* required for animated text */
	private String paramUrl;
	private int paramSpeed;

	/* For large text */
	private int paramStartID;
	private int paramWidth;
	private int paramHeight;
	private int largeTextNumber;

	private String paramScreensList;
	private static final Log log = LogFactory.getLog(TextController.class);

	private boolean createDefaultText(HttpServletRequest request, ModelMap model) {

		paramScenarioId = Integer.parseInt(request.getParameter("scenarioId"));
		paramSceneNumber = Integer
				.parseInt(request.getParameter("sceneNumber"));
		// set all values to default first
		paramXml = "";
		if (Utils.isNotEmpty(request.getParameter("paramScreenID"))) {
			paramScreenID = Integer.parseInt(request
					.getParameter("paramScreenID"));
		} else {
			paramScreenID = getFirstEmptyScreenID(paramScenarioId, paramSceneNumber);
		}
		setScreensUsed(paramScenarioId, paramSceneNumber, model);
		paramScreensList = paramStartID + "";
		paramText = "";
		paramStyle = "";
		paramStartBgColor = "0,0,0";
		paramStartTxtColor = "255,255,255";
		paramUrl = "";
		paramSpeed = 10000;
		// POST
		if (isPost(request)) {
			setAction(request, model);
			paramStartBgColor = request.getParameter("bg_color");
			paramStartTxtColor = request.getParameter("txt_color");
			paramStyle = request.getParameter("style");
			paramScreensList = request.getParameter("screenSelectorScreenList");
			paramText = request.getParameter("text");
			if (Utils.isEmpty(paramText)) {
				error("Text can not be empty", model);
				return false;
			}
			screen = new Screen();
			screen.setId(Utils.convertToScreenIDList(paramScreensList).get(0));
			paramScreenID = screen.getId().getId();
		}
		// no errors
		return true;
	}

	private boolean createDefaultLargeText(HttpServletRequest request, ModelMap model) {
		paramScenarioId = Integer.parseInt(request.getParameter("scenarioId"));
		paramSceneNumber = Integer
				.parseInt(request.getParameter("sceneNumber"));
		// set all values to default first
		paramXml = "";
		paramStartID = getFirstEmptyScreenID(paramScenarioId, paramSceneNumber);
		setScreensUsed(paramScenarioId, paramSceneNumber, model);
		paramScreensList = paramStartID + "";
		paramText = "";
		paramStyle = "";
		paramStartBgColor = "0,0,0";
		paramStartTxtColor = "255,255,255";

		// POST
		if (isPost(request)) {
			setAction(request, model);
			paramStartBgColor = request.getParameter("bg_color");
			paramStartTxtColor = request.getParameter("txt_color");
			paramStyle = request.getParameter("style");
			paramScreensList = request.getParameter("screenSelectorScreenList");

			Rectangle rectangle = ScreenIDCalculator.tryToGetRectangleFromListOfScreenIDs(Utils.convertToScreenIDList(paramScreensList));

			paramStartID = rectangle.getStart().getId();
			paramWidth = rectangle.getWidth();
			paramHeight = rectangle.getHeight();

			paramText = request.getParameter("text");

			if (Utils.isEmpty(paramText)) {
				error("Text can not be empty", model);
				return false;
			}

		}
		// no errors
		return true;
	}

	@RequestMapping(value = "newSimpleText.do")
	public String newSimpleText(HttpServletRequest request, ModelMap model) {
		String editMode= "";
		if(Utils.isNotEmpty(request.getParameter("editMode"))){
			editMode = request.getParameter("editMode");
		}
		if(!editMode.contains("edit")){
			if (createDefaultText(request, model)) {
				// POST
				if (isPost(request)) {
					Text simpleText = new Text(paramText, paramStyle);
					screen.setText(simpleText);
					paramXml = Utils.getXml(screen);
					if (ACTION_SAVE.equals(action)) {
						return addScreenAndSave(paramScenarioId, paramSceneNumber,
								screen);
					}
				}
			}

		}
		else {
			textNumber = Integer.parseInt(request.getParameter("textNumber"));
			editTextPageHelper(request, model);
			if (isPost(request)) {
				paramText = request.getParameter("text");
				paramStyle =  request.getParameter("style");
				Text simpleText = new Text(paramText, paramStyle);
				screen = new Screen();
				screen.setId(Utils.convertToScreenIDList(paramScreensList).get(0));
				paramScreenID = screen.getId().getId();
				screen.setText(simpleText);
				paramXml = Utils.getXml(screen);
				if (ACTION_SAVE.equals(action)) {
					return editScreenAndSave(paramScenarioId, paramSceneNumber,
							screen, textNumber);
				}
			}

		}
		setParamsForView(model);
		model.addAttribute("screenSelectorScreenList", paramScreenID);
		return "newSimpleText.jsp";
	}

	@RequestMapping(value = "newLargeText.do")
	public String newLargeText(HttpServletRequest request, ModelMap model) {
		if (Utils.isEmpty(request.getParameter("height")) && Utils.isEmpty(request.getParameter("width"))) {
			paramHeight = 1;
			paramWidth = 1;
		}
		String editMode= "";
		if(Utils.isNotEmpty(request.getParameter("editMode"))){
			editMode = request.getParameter("editMode");
		}
		if(!editMode.contains("edit")){
			if (createDefaultLargeText(request, model)) {
				// POST
				if (isPost(request)) {
					LargeText largeText = new LargeText(paramText, paramStyle, new ScreenID(paramStartID), paramWidth, paramHeight);
					paramXml = Utils.getXml(largeText);
					if (ACTION_SAVE.equals(action)) {
						return addLargeTextAndSave(paramScenarioId, paramSceneNumber,
								largeText);
					}
				}
			}
			setParamsForView(model);
			List<ScreenID> list = ScreenIDCalculator.getScreenIDList(new Rectangle(new ScreenID(paramStartID), paramHeight, paramWidth));
			model.addAttribute("screenSelectorScreenList", Utils.convertToString(list));
		}
		else{
			editTextPageHelper(request, model);
			setParamsForView(model);
			if (isPost(request)) {
				if (ACTION_SAVE.equals(action)) {
					paramStartBgColor = request.getParameter("bg_color");
					paramStartTxtColor = request.getParameter("txt_color");
					paramStyle = request.getParameter("style");
					paramScreensList = request.getParameter("screenSelectorScreenList");

					Rectangle rectangle = ScreenIDCalculator.tryToGetRectangleFromListOfScreenIDs(Utils.convertToScreenIDList(paramScreensList));

					paramStartID = rectangle.getStart().getId();
					paramWidth = rectangle.getWidth();
					paramHeight = rectangle.getHeight();

					paramText = request.getParameter("text");
					if (Utils.isEmpty(paramText)) {
						error("Text can not be empty", model);
						model.addAttribute("editMode", editMode);
						model.addAttribute("largeTextNumber", largeTextNumber);
						setParamsForView(model);
						return "newLargeText.jsp";

					}
					LargeText largeText = new LargeText(paramText, paramStyle, new ScreenID(paramStartID), paramWidth, paramHeight);
					paramXml = Utils.getXml(largeText);
					return editLargeTextAndSave(paramScenarioId, paramSceneNumber,largeText, largeTextNumber);
				}
			}
		}
		return "newLargeText.jsp";


	}

	private void editTextPageHelper(HttpServletRequest request, ModelMap model) {
		paramScenarioId = Integer.parseInt(request.getParameter("scenarioId"));
		paramSceneNumber = Integer.parseInt(request.getParameter("sceneNumber"));
		Scenario scenario = database.getScenario(paramScenarioId);
		Scene scene = scenario.getSceneById(paramSceneNumber-1);
		if(Utils.isNotEmpty(request.getParameter("largeTextNumber"))){
			largeTextNumber = Integer.parseInt(request.getParameter("largeTextNumber"));
			largeTextHelper(scene, request, model);
		}
		else if (Utils.isNotEmpty(request.getParameter("textNumber"))) {
			textNumber = Integer.parseInt(request.getParameter("textNumber"));
			textHelper(scene, request, model);
		}
		else if (Utils.isNotEmpty(request.getParameter("animatedTextNumber"))) {
			animatedTextNumber = Integer.parseInt(request.getParameter("animatedTextNumber"));
			animatedTextHelper(scene, request, model);
		}
		else{
			error("text number not found", model);
			throw new IllegalArgumentException("Large Image number not found");
		}

	}

	private void textHelper(Scene scene, HttpServletRequest request, ModelMap model){
		Screen screen = scene.getScreens().get(textNumber);
		paramXml = Utils.getXml(screen);
		paramStartID = screen.getId().getId();
		model.addAttribute("screenSelectorScreenList", paramStartID);
		paramText = screen.getText().getText();
		paramStyle = screen.getText().getStyle();
		String delims = "[()]+";
		String[] tokens = paramStyle.split(delims);
		paramStartBgColor = tokens[1];
		paramStartTxtColor = tokens[3];
		setScreensUsed(paramScenarioId, paramSceneNumber, model);
		setAction(request, model);		
	}


	private void animatedTextHelper(Scene scene, HttpServletRequest request, ModelMap model){
		screen = scene.getScreens().get(animatedTextNumber);
		paramXml = Utils.getXml(screen);
		if(Utils.isNotEmpty(request.getParameter("screenSelectorScreenList"))){
			paramScreensList = request.getParameter("screenSelectorScreenList");
			paramStartID = Utils.convertToScreenIDList(paramScreensList).get(0).getId();
		} else {
			paramStartID = screen.getId().getId();
		}
		paramScreenID = paramStartID;
		model.addAttribute("screenSelectorScreenList", paramStartID);
		paramText = screen.getAnimatedText().getText().getText();
		paramStyle = screen.getAnimatedText().getText().getStyle();
		paramUrl = screen.getAnimatedText().getImage().getUrl().toString();
		paramSpeed = screen.getAnimatedText().getSpeed();
		String delims = "[()]+";
		String[] tokens = paramStyle.split(delims);
		paramStartBgColor = tokens[1];
		paramStartTxtColor = tokens[3];
		setScreensUsed(paramScenarioId, paramSceneNumber, model);
		setAction(request, model);		
	}

	private void largeTextHelper(Scene scene, HttpServletRequest request, ModelMap model){
		LargeText largeText = scene.getLargetexts().get(largeTextNumber);
		paramXml = Utils.getXml(largeText);
		paramStartID = largeText.getDisplayarea().getRectangle().getStart().getId();
		paramWidth = largeText.getDisplayarea().getRectangle().getWidth();
		paramHeight = largeText.getDisplayarea().getRectangle().getHeight();

		List<ScreenID> list = ScreenIDCalculator.getScreenIDList(new Rectangle(new ScreenID(paramStartID), paramHeight, paramWidth));
		model.addAttribute("screenSelectorScreenList", Utils.convertToString(list));
		paramText = largeText.getText();
		paramStyle = largeText.getStyle();
		String delims = "[()]+";
		String[] tokens = paramStyle.split(delims);
		paramStartBgColor = tokens[1];
		paramStartTxtColor = tokens[3];
		setScreensUsed(paramScenarioId, paramSceneNumber, model);
		setAction(request, model);		
	}

	private String addLargeTextAndSave(int scenarioId,
			int sceneNumber, LargeText largeText) {
		Scenario scenario = database.getScenario(scenarioId);
		Scene scene = scenario.getScenes().get(sceneNumber - 1);
		List<LargeText> iList = scene.getLargetexts();
		if (iList == null) {
			iList = new ArrayList<LargeText>();
		}
		iList.add(largeText);
		scene.setLargetexts(iList);
		database.saveOrUpdateScenario(scenario);
		return "showScenario.do?id=" + scenario.getId();
	}

	private String editLargeTextAndSave(int scenarioId,int sceneNumber, LargeText largeText, int textIndex) {
		Scenario scenario = database.getScenario(scenarioId);
		Scene scene = scenario.getScenes().get(sceneNumber - 1);
		List<LargeText> iList = scene.getLargetexts();
		iList.remove(textIndex);
		iList.add(textIndex, largeText);
		scene.setLargetexts(iList);
		database.saveOrUpdateScenario(scenario);
		return "showScenario.do?id=" + scenario.getId();
	}

	@RequestMapping(value = "newAnimatedText.do")
	public String newAnimatedText(HttpServletRequest request, ModelMap model) {
		//Check if edit 
		String editMode= "";
		if(Utils.isNotEmpty(request.getParameter("editMode"))){
			editMode = request.getParameter("editMode");
		}
		if(!editMode.contains("edit")){
			if (createDefaultText(request, model)) {
				// POST
				if (isPost(request)) {
					if (Utils.isNotEmpty(request.getParameter("speed"))) {
						paramSpeed = Integer.parseInt(request.getParameter("speed"));
					}
					if (Utils.isNotEmpty(request.getParameter("imageUrl"))) {
						paramUrl = request.getParameter("imageUrl");
					}
					URL imageURL = null;
					try {
						imageURL = new URL(paramUrl);
						AnimatedText animatedText = new AnimatedText(paramText,
								paramStyle, imageURL, new Integer(paramSpeed));
						screen.setAnimatedText(animatedText);
						paramXml = Utils.getXml(screen);
						if (ACTION_SAVE.equals(action)) {
							return addScreenAndSave(paramScenarioId,
									paramSceneNumber, screen);
						}
					} catch (MalformedURLException e) {
						error("Url is not valid", model);
					}
				}
			}
		}
		//Edit-- populate page according to preset values
		else {
			animatedTextNumber = Integer.parseInt(request.getParameter("animatedTextNumber"));
			editTextPageHelper(request, model);
			if (isPost(request)) {
				if (ACTION_SAVE.equals(action)) {
					try {
						if (Utils.isNotEmpty(request.getParameter("speed"))) {
							paramSpeed = Integer.parseInt(request.getParameter("speed"));
						} else {
							throw new Exception ("Speed cannot be blank");
						}
						if (Utils.isNotEmpty(request.getParameter("text"))) {
							paramText = request.getParameter("text");
						} else {
							throw new Exception ("Text cannot be blank");
						}
						if (Utils.isNotEmpty(request.getParameter("style"))) {
							paramStyle = request.getParameter("style");
						}
						if (Utils.isNotEmpty(request.getParameter("imageUrl"))) {
							paramUrl = request.getParameter("imageUrl");
						}
						URL imageURL = null;
						imageURL = new URL(paramUrl);
						
						AnimatedText animatedText = new AnimatedText(paramText,
								paramStyle, imageURL, new Integer(paramSpeed));
						screen.setAnimatedText(animatedText);
						screen.setId(new ScreenID(paramStartID));
						paramXml = Utils.getXml(screen);
						if (ACTION_SAVE.equals(action)) {
							return editScreenAndSave(paramScenarioId,
									paramSceneNumber, screen, animatedTextNumber);
						}
					} catch (MalformedURLException e) {
						model.addAttribute("editMode", editMode);
						model.addAttribute("animatedTextNumber", animatedTextNumber);
						error("Url is not valid", model);
					} catch (Exception e) {
						model.addAttribute("editMode", editMode);
						model.addAttribute("animatedTextNumber", animatedTextNumber);
						error(e.getMessage(), model);
						log.error(e);
						
					}
				}
			}

		}
		setParamsForView(model);
		model.addAttribute("screenSelectorScreenList", paramScreenID);
		return "newAnimatedText.jsp";
	}

	private void setParamsForView(ModelMap model) {
		// params for view
		model.addAttribute("paramXml", paramXml);
		model.addAttribute("paramText", paramText);
		model.addAttribute("paramStyle", paramStyle);
		model.addAttribute("paramScreenID", paramScreenID);
		model.addAttribute("paramScenarioId", paramScenarioId);
		model.addAttribute("paramSceneNumber", paramSceneNumber);
		model.addAttribute("paramColors", paramColors);
		model.addAttribute("paramStartBgColor", paramStartBgColor);
		model.addAttribute("paramStartTxtColor", paramStartTxtColor);
		model.addAttribute("paramSpeed", paramSpeed);
		model.addAttribute("paramUrl", paramUrl);

		/* For large text */
		model.addAttribute("paramStartID", paramStartID);
		model.addAttribute("paramWidth", paramWidth);
		model.addAttribute("paramHeight", paramHeight);

	}

}
