package com.hsf302.student.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueStudentCodeValidator.class)
public @interface UniqueStudentCode {

    String message() default "{student.code.duplicate}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
