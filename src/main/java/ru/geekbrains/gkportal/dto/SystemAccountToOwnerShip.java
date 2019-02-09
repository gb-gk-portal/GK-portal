package ru.geekbrains.gkportal.dto;


import lombok.Data;
import ru.geekbrains.gkportal.entity.ContactType;
import ru.geekbrains.gkportal.validation.ValidPhoneNumber;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@Data

public class SystemAccountToOwnerShip {

    private final static String MIN_MAX_VALIDATION_MESSAGE = "от {min} до {max} символов";
    private final static String MIN_VALIDATION_MESSAGE = "Минимум {min} символов";

    List<OwnershipRegDTO> ownerships = new ArrayList<>();

    @NotNull(message = "Поле обязательно")
    @Size(min = 2, max = 25, message = MIN_MAX_VALIDATION_MESSAGE)
    private String firstName;

    @NotNull(message = "Поле обязательно")
    @Size(min = 2, max = 25, message = MIN_MAX_VALIDATION_MESSAGE)
    private String lastName;

//    @Size(min = 2, max = 25, message = MIN_MAX_VALIDATION_MESSAGE)
    private String middleName;

    @Email(message = "Почта указана не корректна")
    @NotNull(message = "Поле обязательно")
    @Size(min = 5, max = 25, message = MIN_MAX_VALIDATION_MESSAGE)
    private String email;

    @ValidPhoneNumber(message = "Телефон указан не корректно")
    @NotNull(message = "Поле обязательно")
    @Size(min = 10, message = MIN_VALIDATION_MESSAGE)
    private String phoneNumber;

    private ContactType contactType;
    //private Boolean boughtParkingPlace;

    private Boolean allowContactsSharing;

    private AnswerResultDTO answerResultDTO;

    private String uuid;
    // Пока бесполезная фигня, но потом прикрутим.

    //private List<String> interestedIn;

    //private String extraInterests;

    //private Integer carsInFamily;

    // private String comments;


    public SystemAccountToOwnerShip() {

    }
}
