package com.island.bookingapi.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


/**
 * Validation annotation to validate if date range is valid.
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
@Documented
public @interface ValidateDateRange {
    String message() default "Invalid dates";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return ArrivalDate
     */
    String arrivalDate();

    /**
     * @return departureDate
     */
    String departureDate();

}

