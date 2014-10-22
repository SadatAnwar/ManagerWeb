package de.fraunhofer.iao.muvi.managerweb.domain;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

@XmlRootElement(name = "muvi")
@XmlAccessorType(XmlAccessType.FIELD)
public class MuVi {

	@XmlElement(name = "scenario")
	private List<Scenario> scenarios;
	
	@XmlElement(name = "scene")
	private List<Scene> scenes;
	
	@XmlElement(name = "screen")
	private List<Screen> screens;
	
	public List<Scenario> getScenarios() {
		return scenarios;
	}
	public void setScenarios(List<Scenario> scenarios) {
		this.scenarios = scenarios;
	}
	public List<Scene> getScenes() {
		return scenes;
	}
	public void setScenes(List<Scene> scenes) {
		this.scenes = scenes;
	}
	public List<Screen> getScreens() {
		return screens;
	}
	public void setScreens(List<Screen> screens) {
		this.screens = screens;
	}
	
	public static void main(String[] args) throws MalformedURLException {
		Scenario s = new Scenario();
		s.setName("Maxou");
		s.setDate(new Date());
		Scene sc = new Scene();
		sc.setName("Scene 1");
		
		LargeImage limg = new LargeImage();
		limg.setUrl(new URL("http://test.com/test.png"));
		
		ScreenID start = new ScreenID();
		start.setColumn(3);
		start.setRow(2);
		
		Rectangle rec = new Rectangle();
		rec.setStart(start);
		rec.setHeight(2);
		rec.setWidth(3);
		
		DisplayArea da = new DisplayArea();
		da.setRectangle(rec);
		
		limg.setDisplayarea(da);
		
		List<LargeImage> limgl = new ArrayList<>();
		limgl.add(limg);
		
		sc.setLargeimages(limgl);
		
		List<Scene> scenes = new ArrayList<>();
		scenes.add(sc);
		
		Scene s2 = new Scene();
		SearchResults sr = new SearchResults();
		sr.setType(SearchResultType.GoogleImages);
		sr.setQuery("Maxou");
		sr.setDisplayarea(da);
		List<SearchResults> searchresults = new ArrayList<>();
		searchresults.add(sr);
		s2.setSearchresults(searchresults);
		s2.setName("Scene 2");
		scenes.add(s2);
		
		Scene s3 = new Scene();
		
		Screen scr1 = new Screen();
		scr1.setId(new ScreenID(2));
		scr1.setText(new Text("Hello"));
		
		Screen scr2 = new Screen();
		scr2.setId(new ScreenID(3));
		scr2.setUrl(new URL("http://www.iao.fraunhofer.de/"));
		
		Screen scr3 = new Screen();
		scr3.setId(new ScreenID(4));
		Image image = new Image();
		image.setUrl(new URL("http://www.fraunhofer.de/logo.png"));
		image.setScale(Scale.fullscreen);
		scr3.setImage(image);
		
		Screen scr4 = new Screen();
		scr4.setId(new ScreenID(7));
		AnimatedText animatedText = new AnimatedText("Jan Finzen", Text.STYLE_FRAUNHOFER_ON_WHITE, new URL("http://www.fraunhofer.de/logo.png"), 20000);
		scr4.setAnimatedText(animatedText);
		
		List<Screen> screens = new ArrayList<>();
		screens.add(scr1);
		screens.add(scr2);
		screens.add(scr3);
		screens.add(scr4);
		
		s3.setScreens(screens);
		
		scenes.add(s3);
		
		s.setScenes(scenes);
		
		MuVi muvi = new MuVi();
		List<Scenario> scenarios = new ArrayList<>();
		scenarios.add(s);
		muvi.setScenarios(scenarios);
		muvi.setScreens(screens);
		
		System.out.println(Utils.getXml(muvi));
				
		String file = MuVi.class.getResource("MuVi.class").getFile();
		file = file.replaceAll("de/fraunhofer/iao/muvi/managerweb/domain/MuVi.class", "MuViDL_v1.1.xsd");
		File schemFile = new File(file);
		String xml = Utils.getXml(muvi, schemFile, true);
		System.out.println(xml);
		
	}
	

}
