package by.clevertec.house.config;

import java.util.Objects;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

/**
 * Фабрика для создания источников свойств на основе YAML.
 * Используется для чтения свойств из файла application.yml.
 */
public class YamlPropertySourceFactory implements PropertySourceFactory {
    /**
     * Создает и возвращает источник свойств на основе указанного ресурса.
     *
     * @return источник свойств.
     */
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(new ClassPathResource("application.yml"));
        return new PropertiesPropertySource("custom", Objects.requireNonNull(factory.getObject()));
    }
}
