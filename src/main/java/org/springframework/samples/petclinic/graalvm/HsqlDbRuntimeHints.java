package org.springframework.samples.petclinic.graalvm;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class HsqlDbRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints
            .reflection()
            .registerTypeIfPresent(
                classLoader,
                "org.hibernate.dialect.HSQLDialect",
                MemberCategory.DECLARED_FIELDS,
                MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                MemberCategory.INVOKE_DECLARED_METHODS
            )
            .registerTypeIfPresent(
                classLoader,
                "org.hsqldb.dbinfo.DatabaseInformationFull",
                MemberCategory.DECLARED_FIELDS,
                MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                MemberCategory.INVOKE_DECLARED_METHODS
            );
        hints
            .resources()
            .registerPattern("db/hsqldb/**")
            .registerPattern("org/hsqldb/resources/information-schema.sql")
            .registerPattern("org/hsqldb/resources/lob-schema.sql")
            .registerResourceBundle("org.hsqldb.resources.sql-state-messages");
    }

}
