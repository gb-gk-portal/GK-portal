package ru.geekbrains.gkportal.exception;

import java.util.NoSuchElementException;

public class CommunicationTypeNotFoundException extends NoSuchElementException {

    private final static String MESSAGE = "Description %s not found";

    public CommunicationTypeNotFoundException(String description) {
        super(String.format(MESSAGE, description));
    }

}
