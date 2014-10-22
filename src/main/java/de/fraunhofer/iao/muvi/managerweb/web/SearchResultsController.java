/**
 * 
 */
package de.fraunhofer.iao.muvi.managerweb.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import de.fraunhofer.iao.muvi.managerweb.domain.DisplayArea;
import de.fraunhofer.iao.muvi.managerweb.domain.Rectangle;
import de.fraunhofer.iao.muvi.managerweb.domain.Scenario;
import de.fraunhofer.iao.muvi.managerweb.domain.Scene;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.domain.SearchResultType;
import de.fraunhofer.iao.muvi.managerweb.domain.SearchResults;
import de.fraunhofer.iao.muvi.managerweb.logic.ScreenIDCalculator;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

@Controller
public class SearchResultsController extends MainController {

	private static final Log log = LogFactory
			.getLog(SearchResultsController.class);

	@RequestMapping(value = "newSearchResults.do")
	public String newLargeImage(HttpServletRequest request, ModelMap model) {
		try {
			int paramScenarioId = Integer.parseInt(request
					.getParameter("scenarioId"));
			int paramSceneNumber = Integer.parseInt(request
					.getParameter("sceneNumber"));
			String paramXml = "";
			String paramType = "";
			int paramScreenID = getFirstEmptyScreenID(paramScenarioId,
					paramSceneNumber);
			int paramStartID = paramScreenID;
			int paramWidth = 1;
			int paramHeight = 1;
			String paramQuery = "";
			// POST
			if (isPost(request)) {
				setAction(request, model);
				paramQuery = request.getParameter("query");
				paramType = request.getParameter("type");
				if (Utils.isEmpty(paramQuery)) {
					error("Query can not be empty", model);
				} else if (Utils.isEmpty(paramType)) {
					error("Type can not be empty", model);
				} else {
					if (Utils.isNotEmpty(request.getParameter("screenID"))) {
						paramScreenID = Integer.parseInt(request
								.getParameter("paramScreenID"));
					}
					if (Utils.isNotEmpty(request.getParameter("start"))) {
						paramStartID = Integer.parseInt(request.getParameter("start"));
					}
					if (Utils.isNotEmpty(request.getParameter("width"))) {
						paramWidth = Integer.parseInt(request.getParameter("width"));
					}
					if (Utils.isNotEmpty(request.getParameter("height"))) {
						paramHeight = Integer.parseInt(request.getParameter("height"));
					}

					SearchResults sr = new SearchResults();
					sr.setQuery(paramQuery);
					sr.setSearchscreen(new ScreenID(paramScreenID));
					sr.setType(SearchResultType.valueOf(paramType));
					DisplayArea displayarea = new DisplayArea(new Rectangle(
							new ScreenID(paramStartID),
							paramHeight,
							paramWidth));
					sr.setDisplayarea(displayarea);

					paramXml = Utils.getXml(sr);
					if (ACTION_SAVE.equals(action)) {
						Scenario scenario = database
								.getScenario(paramScenarioId);
						Scene scene = scenario.getScenes().get(
								paramSceneNumber - 1);
						List<SearchResults> iList = scene.getSearchresults();
						if (iList == null) {
							iList = new ArrayList<SearchResults>();
						}
						iList.add(sr);
						scene.setSearchresults(iList);
						database.saveOrUpdateScenario(scenario);
						return "showScenario.do?id=" + scenario.getId();
					}
				}
				// set params for show
				model.addAttribute("paramXml", paramXml);
			}
			// delegate variable to view
			model.addAttribute("paramType", paramType);
			model.addAttribute("paramQuery", paramQuery);
			model.addAttribute("paramScreenID", paramScreenID);
			model.addAttribute("paramStartID", paramStartID);
			model.addAttribute("paramWidth", paramWidth);
			model.addAttribute("paramHeight", paramHeight);
			model.addAttribute("paramScenarioId", paramScenarioId);
			model.addAttribute("paramSceneNumber", paramSceneNumber);
			model.addAttribute("typeValues", SearchResultType.values());
			setScreensUsed(paramScenarioId, paramSceneNumber, model);
			List<ScreenID> list = ScreenIDCalculator.getScreenIDList(new Rectangle(new ScreenID(paramStartID), paramHeight, paramWidth));
			model.addAttribute("screenSelectorScreenList", Utils.convertToString(list));
		} catch (Exception e) {
			String message = "Error: one mandatory parameter was not properly set: "
					+ e.getMessage();
			log.error(message, e);
			error(message, model);
		}
		return "newSearchResults.jsp";
	}

}
