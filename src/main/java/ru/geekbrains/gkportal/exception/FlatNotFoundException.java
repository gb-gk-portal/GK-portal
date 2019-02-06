package ru.geekbrains.gkportal.exception;

import ru.geekbrains.gkportal.DTO.FlatRegDTO;
import ru.geekbrains.gkportal.entities.Flat;
import ru.geekbrains.gkportal.entities.SystemAccount;

import java.util.NoSuchElementException;

public class FlatNotFoundException extends NoSuchElementException {

    private final static String MESSAGE = "House %d, Porch %d, Floor %d, Flat %d not found";

    public FlatNotFoundException(Flat flat) {
        super(String.format(MESSAGE,
                flat.getHouse(),
                flat.getPorch(),
                flat.getFloor(),
                flat.getFlatNumber())
        );
    }

    public FlatNotFoundException(FlatRegDTO flatRegDTO) {
        super(String.format(MESSAGE,
                flatRegDTO.getHousingNumber(),
                flatRegDTO.getPorchNumber(),
                flatRegDTO.getFloorNumber(),
                flatRegDTO.getFlatNumber())
        );
    }

}
