package com.maximogiordano.users.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Pattern(regexp = "[^A-Z]*[A-Z][^A-Z]*", message = "must contain exactly one uppercase letter")
@Pattern(regexp = "\\D*\\d\\D*\\d\\D*", message = "must contain exactly two digits")
@Pattern(regexp = "[a-zA-Z0-9]*", message = "only letters and digits allowed")
@Size(min = 8, max = 12, message = "must contain at least {min} and at most {max} characters")
public @interface Password {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
