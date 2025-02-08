package com.maximogiordano.users.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniquePhonesValidator.class)
public @interface UniquePhones {
    String message() default "there are duplicate phones in the list";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
