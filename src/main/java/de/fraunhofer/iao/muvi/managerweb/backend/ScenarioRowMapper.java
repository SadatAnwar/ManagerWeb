package de.fraunhofer.iao.muvi.managerweb.backend;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import de.fraunhofer.iao.muvi.managerweb.domain.Scenario;

public class ScenarioRowMapper implements RowMapper<Scenario> {

	@Override
	public Scenario mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Scenario scenario = new Scenario();
		scenario.setId(rs.getInt("id"));
		scenario.setDate(rs.getDate("date"));
		scenario.setName(rs.getString("name"));
		scenario.setDescription(rs.getString("description"));
		scenario.setTagString(rs.getString("tags"));		

		return scenario;
	}

}
