package by.clevertec.house.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Класс инициализации Dispatcher Servlet.
 * Расширяет AbstractAnnotationConfigDispatcherServletInitializer для настройки DispatcherServlet в Spring MVC.
 */
public class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    /**
     * Получает конфигурационные классы корневого контекста приложения.
     *
     * @return null, так как корневые конфигурационные классы не используются.
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    /**
     * Получает конфигурационные классы для DispatcherServlet.
     *
     * @return массив классов, содержащий SpringConfig.class.
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringConfig.class};
    }

    /**
     * Получает маппинги сервлетов для DispatcherServlet.
     *
     * @return массив строк, содержащий "/".
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
