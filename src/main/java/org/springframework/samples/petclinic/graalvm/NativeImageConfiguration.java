package org.springframework.samples.petclinic.graalvm;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration
@ImportRuntimeHints({
    HsqlDbRuntimeHints.class
})
public class NativeImageConfiguration {
}
