package ru.geekbrains.gkportal.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    private static final String PHONENUMBER_PATTERN = "^$|[0-9]{10}";
    private Pattern pattern;
    private Matcher matcher;

    @Override
    public boolean isValid(final String phoneNumber, final ConstraintValidatorContext context) {

        pattern = Pattern.compile(PHONENUMBER_PATTERN);
        if (phoneNumber == null) {
            return false;
        }

        matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
