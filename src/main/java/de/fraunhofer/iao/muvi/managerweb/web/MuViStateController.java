package de.fraunhofer.iao.muvi.managerweb.web;


import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.logic.MuViState;
import de.fraunhofer.iao.muvi.managerweb.logic.ScreenIDCalculator;

@Controller
public class MuViStateController {

	private static final Log log = LogFactory
			.getLog(MuViStateController.class);

	private MuViState muViState;
	
	@RequestMapping(value = "muvistate.do")
	public String muvistate(HttpServletRequest request, ModelMap model) throws URISyntaxException {
		
		List<String> screenUrls = new ArrayList<String>();
		for (int i=1; i<=ScreenIDCalculator.NUMBER_OF_SCREENS; i++) {
			try {
				URL screenUrl = muViState.getScreenUrl(new ScreenID(i));
				if (screenUrl != null) {
					String urlString = screenUrl.toURI().toString();
					screenUrls.add("\""+URLEncoder.encode(urlString, StandardCharsets.UTF_8.displayName())+"\"");
					model.addAttribute("screen" + i + "URL", URLEncoder.encode(urlString, StandardCharsets.UTF_8.displayName()));
				} else {
					
					String offUrl = muViState.getDatabase().readConfigValue("managerURL") + "apps/textcolor.jsp?text=&style=background-color:black;";
					
					model.addAttribute("screen" + i + "URL", URLEncoder.encode(offUrl, StandardCharsets.UTF_8.displayName()));
					screenUrls.add("\""+URLEncoder.encode(offUrl, StandardCharsets.UTF_8.displayName())+"\"");
				}
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage(), e);
			}
			model.addAttribute("screen" + i + "State", muViState.getScreenState(new ScreenID(i)));
			
		}
		model.addAttribute("urlList",screenUrls);
		log.debug("MuVi state rendered.");
		
		return "muvistateNew.jsp";
	}

	public MuViState getMuViState() {
		return muViState;
	}

	public void setMuViState(MuViState muViState) {
		this.muViState = muViState;
	}
	
}
