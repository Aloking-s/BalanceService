package com.maveric.balanceservice.validation;

import com.maveric.balanceservice.constant.Currency;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class CurrencyValidatorImplementation implements ConstraintValidator<CurrencyValidation,Currency>{

    private Currency[] allCurrencyValue;

    @Override
    public void initialize(CurrencyValidation constraintAnnotation) {
        this.allCurrencyValue = constraintAnnotation.anyOfTheseCurrency();
    }

    @Override
    public boolean isValid(Currency currency, ConstraintValidatorContext constraintValidatorContext) {
        return currency == null || Arrays.asList(allCurrencyValue).contains(currency);
    }


}
