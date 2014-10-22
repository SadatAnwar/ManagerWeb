package de.fraunhofer.iao.muvi.managerweb;

/**
 * Cleans the database by dropping the schema, re-creating the tables and inserting some data.
 * 
 * @author Bertram Frueh
 */
public class DatabaseResetterAndInitializer {

    public static void main(String[] args) throws Exception {

        new DatabaseResetterAndInitializer().run();
    }

    public void run() throws Exception {

        new DatabaseResetter().run();
        new DatabaseInitializer().run();
    }

}
