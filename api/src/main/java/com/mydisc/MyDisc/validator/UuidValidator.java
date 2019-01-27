package com.mydisc.MyDisc.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class UuidValidator implements ConstraintValidator<UuidConstraint, String> {

    @Override
    public void initialize(UuidConstraint id) {
    }

    @Override
    public boolean isValid(String uuid, ConstraintValidatorContext context) {
        Pattern pattern = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
        return pattern.matcher(uuid).matches();
    }
}