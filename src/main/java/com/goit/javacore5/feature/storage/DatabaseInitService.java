package com.goit.javacore5.feature.storage;

import org.flywaydb.core.Flyway;

public class DatabaseInitService {
    public void initDb(String connectionUrl) {
        // Create the Flyway instance and point it to the database
        Flyway flyway = Flyway
                .configure()
                .dataSource(connectionUrl, null, null)
                .load();

        // Start the migration
        flyway.migrate();
    }
}
