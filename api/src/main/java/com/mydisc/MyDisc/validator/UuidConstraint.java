package com.mydisc.MyDisc.validator;

import java.lang.annotation.*;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = UuidValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface UuidConstraint {
    String message() default "Not a uuid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
