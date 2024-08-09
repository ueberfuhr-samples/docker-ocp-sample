package org.springframework.samples.petclinic.graalvm;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class SwaggerUiRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints
            .resources()
            .registerPattern("swagger-ui.html")
            .registerPattern("swagger-ui/**")
            .registerPattern("META-INF/resources/webjars/**");
    }

}
