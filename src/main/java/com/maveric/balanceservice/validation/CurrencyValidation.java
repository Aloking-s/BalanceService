package com.maveric.balanceservice.validation;

import com.maveric.balanceservice.constant.Currency;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { CurrencyValidatorImplementation.class })
public @interface CurrencyValidation {
    Currency[] anyOfTheseCurrency();
    String message() default "Currency should be any of {anyOfTheseCurrency}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
