package de.fraunhofer.iao.muvi.managerweb.backend;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Little helper methods for database access.
 * 
 * @author Bertram Frueh
 */
public class DatabaseUtils {

    /**
     * Reads the given column from the result set. Returns {@code null}, if the SQL value was null.
     */
    public static Integer getInteger(ResultSet rs, String columnName) throws SQLException {

        Integer value = rs.getInt(columnName);
        if (rs.wasNull()) {
            value = null;
        }
        return value;
    }
}
