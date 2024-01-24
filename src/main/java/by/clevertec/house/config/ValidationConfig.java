package by.clevertec.house.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * Конфигурационный класс для настройки валидации.
 */
@Configuration
public class ValidationConfig {

    /**
     * Создает и возвращает экземпляр MethodValidationPostProcessor.
     *
     * @return экземпляр MethodValidationPostProcessor.
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    /**
     * Создает и возвращает экземпляр LocalValidatorFactoryBean.
     *
     * @return экземпляр LocalValidatorFactoryBean.
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}
