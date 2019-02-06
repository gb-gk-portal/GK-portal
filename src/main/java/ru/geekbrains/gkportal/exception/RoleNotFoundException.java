package ru.geekbrains.gkportal.exception;

import java.util.NoSuchElementException;

public class RoleNotFoundException extends NoSuchElementException {

    private final static String MESSAGE = "Description %s not found";

    public RoleNotFoundException(String description) {
        super(String.format(MESSAGE, description));
    }

}
