package by.clevertec.house.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${flyway.enabled:false}")
    private boolean isFlywayEnabled;

    @Bean
    public Flyway flyway() {
        if (!isFlywayEnabled) {
            return null;
        }

        Flyway flyway = Flyway.configure()
                .dataSource(url, username, password)
                .baselineVersion("0")
                .load();
        flyway.baseline();
        flyway.migrate();

        return flyway;
    }
}
