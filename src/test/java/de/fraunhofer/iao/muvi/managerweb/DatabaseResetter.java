package de.fraunhofer.iao.muvi.managerweb;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import de.fraunhofer.iao.muvi.managerweb.utils.Config;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

/**
 * Resets the database by dropping the schema and re-creating the schema and the tables defined in
 * dbschema.sql.
 */
public class DatabaseResetter {

    private static final Log log = LogFactory.getLog(DatabaseInitializer.class);

    protected ClassPathXmlApplicationContext context;

    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {

        new DatabaseResetter().run();
    }

    public void run() {

        log.info("Starting");

        start();
        try {

            if (Utils.isNotEmpty(Config.getProperty("system.resetMode"))) {
                dropDatabase();

                log.info("Database deleted.");

                createDatabase();

                log.info("Database created.");
            } else {
                log.info("Do not drop complete database, only enpty specific tables.");
                emptyTables();
                log.info("Current day tables were emptied.");
            }

        } finally {
            stop();
        }

        log.info("Finished");
    }

    private void start() {

        /* We need an application context where the database schema isn't specified.*/
        context = new ClassPathXmlApplicationContext("monitoring-junit-context.xml");
        DataSource dataSource = context.getBean("dataSource", DataSource.class);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private void stop() {

        if (context != null) {
            context.close();
        }
    }

    /**
     * Drop the "monitoring" schema from the database.
     */
    private void dropDatabase() {

        String schema = jdbcTemplate.queryForObject("select SCHEMA()", String.class);
        jdbcTemplate.execute("DROP SCHEMA IF EXISTS " + schema);
    }

    private void emptyTables() {

        jdbcTemplate.execute("DELETE FROM monitoring.t_check;");
        log.info("t_check emptied.");
        jdbcTemplate.execute("DELETE FROM monitoring.t_alert;");
        log.info("t_alert emptied.");
        jdbcTemplate.execute("DELETE FROM monitoring.t_event;");
        log.info("t_event emptied.");
    }

    /**
     * Create the monitoring schema from the .sql script file. Each SQL statement is executed
     * independently.
     */
    private void createDatabase() {

        try {
            String schemaFileName = "dbschema.sql";
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(schemaFileName);
            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer, StandardCharsets.UTF_8);
            String sqlScript = writer.toString();
            /* We need to execute each statement independently */
            String[] sqlStatements = sqlScript.split(";");
            int i = 1;
            for (String sql : sqlStatements) {
                if (sql.trim().length() > 0) {
                    log.debug("Executing statement " + i + " / " + sqlStatements.length);
                    jdbcTemplate.execute(sql);
                } else {
                    log.debug("Skipping empty statement " + i + " / " + sqlStatements.length);
                }
                i++;
            }
        } catch (IOException e) {
            log.error(e);
        }
    }

}
