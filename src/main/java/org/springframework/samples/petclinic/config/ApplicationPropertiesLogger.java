package org.springframework.samples.petclinic.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
@Profile("logprops")
public class ApplicationPropertiesLogger {

    private static final Logger log = LoggerFactory.getLogger(ApplicationPropertiesLogger.class);

    @EventListener
    public void handleContextRefreshed(ContextRefreshedEvent event) {
        printActiveProperties((ConfigurableEnvironment) event.getApplicationContext().getEnvironment());
    }

    private void printActiveProperties(ConfigurableEnvironment env) {

        log.info("************************* ACTIVE APP PROPERTIES ******************************");

        List<MapPropertySource> propertySources = new ArrayList<>();

        env
            .getPropertySources()
            .stream()
            .filter(MapPropertySource.class::isInstance)
            .map(MapPropertySource.class::cast)
            .map(MapPropertySource::getSource)
            .map(Map::keySet)
            .flatMap(Collection::stream)
            .distinct()
            .sorted()
            .forEach(key -> {
                try {
                    log.info("{}={}", key, env.getProperty(key));
                } catch (Exception e) {
                    log.warn("{} -> {}", key, e.getMessage());
                }
            });
        log.info("******************************************************************************");
    }

}
