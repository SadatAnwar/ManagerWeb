package de.fraunhofer.iao.muvi.managerweb;

import java.util.Calendar;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import de.fraunhofer.iao.muvi.managerweb.backend.Database;

/**
 * Puts some default values (users etc.) into the database.
 */
public class DatabaseInitializer {

    private static final Log log = LogFactory.getLog(DatabaseInitializer.class);

    protected ClassPathXmlApplicationContext context;
    private JdbcTemplate jdbcTemplate;
    private Database database;
    private Date now;
    private Calendar offset;

    public static void main(String[] args) throws Exception {

        new DatabaseInitializer().run();
    }

    public void run() throws Exception {

        log.info("Starting");

        start();
        try {


        } finally {
            stop();
        }

        log.info("Finished");
    }

    private void start() {

        context = new ClassPathXmlApplicationContext("monitoring-junit-context.xml");
        DataSource dataSource = context.getBean("dataSource", DataSource.class);
        jdbcTemplate = new JdbcTemplate(dataSource);
        database = context.getBean("database", Database.class);
        now = new Date();
    }

    private void stop() {

        if (context != null) {
            context.close();
        }
    }

//    private String springSecuritySha1(String text) {
//
//        return new ShaPasswordEncoder().encodePassword(text, null);
//    }

}
