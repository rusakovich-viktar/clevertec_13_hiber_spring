package by.clevertec.house.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan("by.clevertec.house")
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
@RequiredArgsConstructor
public class SpringConfig implements WebMvcConfigurer {

    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public EntityManager entityManager() {
        return entityManagerFactory.createEntityManager();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
