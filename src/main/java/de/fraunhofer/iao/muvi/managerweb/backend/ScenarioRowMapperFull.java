package de.fraunhofer.iao.muvi.managerweb.backend;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import de.fraunhofer.iao.muvi.managerweb.domain.Scenario;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

public class ScenarioRowMapperFull implements RowMapper<Scenario> {

	@Override
	public Scenario mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Scenario scenario = Utils.getObjectFromXml(rs.getString("xml"), Scenario.class);
		return scenario;
	}

}
