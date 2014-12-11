package de.fraunhofer.iao.muvi.managerweb.backend;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import de.fraunhofer.iao.muvi.managerweb.domain.Scenario;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

/**
 * Access to the MySQL database.
 * 
 * @author Bertram Frueh, Falko Koetter
 */
public class Database {

    private static final Log log = LogFactory.getLog(Database.class);

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {

        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

   
    public String readConfigValue(String name) {
        String result = null;
        String[] params = { name.toString() };
        List<String> results = jdbcTemplate.queryForList(
                "select value from config where name =?", params, String.class);
        if (!results.isEmpty()) {
            result = results.get(0);
        }
        return result;
    }
    
    public boolean readStatusFlag(String name) {
        boolean result = false;
        String[] params = { name.toString() };
        List<String> results = jdbcTemplate.queryForList(
                "select value from config where name =?", params, String.class);
        if (!results.isEmpty()) {
        	if ("1".equals(results.get(0))){
        		result = true;	
        	}
        	else{
        		result = false;
        	}
        }
        return result;
    }
    
    public boolean setStatusFlag(String name, boolean status) {
        int result;
        int value = 0;
        if(status){
        	value=1;
        }
       result = jdbcTemplate.update("UPDATE config SET value = ? WHERE name = ?", value, name);
       if(result == 1){
    	   return true;
       }
       else{
    	   return false;
       }
    }
    
    public void saveOrUpdateConfigValue(String name, String value) {
    	if (readConfigValue(name) != null) {
    		updateConfigValue(name, value);
    	} else {
    		saveConfigValue(name, value);
    	}
    }
    
    public void updateConfigValue(String name, String value) {
    	jdbcTemplate.update("UPDATE config SET value = ? WHERE name = ?", value, name);
    }
    
    public void saveConfigValue(String name, String value) {
    	jdbcTemplate.update("INSERT INTO config(value, name) VALUES (?, ?) ", value, name);
    }
    
    public void deleteScenario(Scenario scenario) {
    	if (scenario != null) {
    		deleteScenario(scenario.getId());
    	}
    }
    
    public void deleteScenario(Integer scenarioId) {
    	if (scenarioId != null && scenarioId > 0) {
    		jdbcTemplate.update("delete from scenarios where id = ?", scenarioId);
    		log.info("Delete scenario " + scenarioId);
    	}
    }
    
    public void saveOrUpdateScenario(Scenario scenario) {
    	@SuppressWarnings("deprecation")
		int count = jdbcTemplate.queryForInt("select count(*) from scenarios where id = ?",
    			 scenario.getId());
         if (count > 0) {
             jdbcTemplate.update(getScenarioUpdateSql(), scenario.getName(), scenario.getDescription(), new Date(), scenario.getTagString(), Utils.getXml(scenario), scenario.getId());
         } else {
             SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(
                     "scenarios").usingGeneratedKeyColumns("id");

             Map<String, Object> parameters = new HashMap<>();
             parameters.put("name", scenario.getName());
             parameters.put("description", scenario.getDescription());
             parameters.put("date", new Date());
             parameters.put("tags", scenario.getTagString());
             parameters.put("xml", Utils.getXml(scenario));

             /* Save the generated primary key. */
             Number key = simpleJdbcInsert.executeAndReturnKey(parameters);
             scenario.setId(key.intValue());
             
             /* Save ID in XML in database */
             saveOrUpdateScenario(scenario);
         }
    }
    
    public List<Scenario> getScenarios() {
    	List<Scenario> scenarios = jdbcTemplate.query("select * from scenarios where id>1 order by date desc",
                new ScenarioRowMapper());
    	return scenarios;
    }
    
    public List<Scenario> getScenarios(String tag) {
    	if (Utils.isEmpty(tag)) {
    		return getScenarios();
    	} else {
	    	List<Scenario> scenarios = jdbcTemplate.query("select * from scenarios where id>1 and tags like ? order by date desc",
	                new ScenarioRowMapper(), "%" + tag + "%");
	    	return scenarios;
    	}
    }
    
    public Scenario getScenario(Integer id) {
    	List<Scenario> scenarios = jdbcTemplate.query("select * from scenarios where id = ?",
                new ScenarioRowMapperFull(), id);
    	if (Utils.isNotEmpty(scenarios)) {
    		return scenarios.get(0);
    	} else {
    		return null;
    	}
    }
    
    private String getScenarioUpdateSql() {
        return "UPDATE scenarios SET name = ?, description = ?, date = ?, tags = ?, xml = ? WHERE id = ?";
    }
    
    public boolean isDebugMode() {
    	return "debugMode".equals(this.readConfigValue("debugMode"));
    }

}
