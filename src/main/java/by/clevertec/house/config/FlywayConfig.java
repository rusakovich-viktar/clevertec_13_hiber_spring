package by.clevertec.house.config;

import lombok.Getter;
import lombok.Setter;

/**
 * The `FlywayConfig` class represents the configuration for Flyway database migration.
 * It contains information about the database URL, username, and password required for migration.
 */
@Getter
@Setter
public class FlywayConfig {
    private String url;
    private String user;
    private String password;

}
