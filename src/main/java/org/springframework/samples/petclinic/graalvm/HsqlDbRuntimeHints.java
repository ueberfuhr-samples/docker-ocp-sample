package org.springframework.samples.petclinic.graalvm;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class HsqlDbRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints
            .resources()
            .registerResourceBundle("org.hsqldb.resources.sql-state-messages");
    }

}
