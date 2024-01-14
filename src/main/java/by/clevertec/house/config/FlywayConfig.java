package by.clevertec.house.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для Flyway.
 * Используется для управления версиями базы данных.
 */
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

    /**
     * Создает и настраивает экземпляр Flyway.
     *
     * @return Настроенный экземпляр Flyway или null, если Flyway отключен.
     */
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
