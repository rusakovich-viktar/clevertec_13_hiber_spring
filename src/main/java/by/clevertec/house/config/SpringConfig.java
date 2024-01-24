package by.clevertec.house.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Конфигурационный класс Spring.
 * Включает в себя настройки Web MVC, сканирование компонентов и свойства приложения.
 */
@Configuration
@EnableWebMvc
@ComponentScan("by.clevertec.house")
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
@RequiredArgsConstructor
public class SpringConfig implements WebMvcConfigurer {

    private final EntityManagerFactory entityManagerFactory;

    /**
     * Создает и возвращает экземпляр EntityManager.
     *
     * @return экземпляр EntityManager.
     */
    @Bean
    public EntityManager entityManager() {
        return entityManagerFactory.createEntityManager();
    }

    /**
     * Создает и возвращает экземпляр ObjectMapper.
     *
     * @return экземпляр ObjectMapper.
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
