package org.springframework.samples.petclinic.graalvm;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.samples.petclinic.model.*;
import org.springframework.samples.petclinic.rest.advice.ExceptionControllerAdvice;

@Configuration
@ImportRuntimeHints({
    HsqlDbRuntimeHints.class,
    TomcatRuntimeHints.class
})
@RegisterReflectionForBinding({
    BaseEntity.class,
    NamedEntity.class,
    Owner.class,
    Person.class,
    Pet.class,
    PetType.class,
    Role.class,
    Specialty.class,
    User.class,
    Vet.class,
    Visit.class,
    ExceptionControllerAdvice.ErrorInfo.class
})
public class NativeImageConfiguration {
}
