package ru.geekbrains.gkportal.entity;


import lombok.Data;
import ru.geekbrains.gkportal.dto.FlatRegDTO;
import ru.geekbrains.gkportal.validation.PasswordsNotEqual;
import ru.geekbrains.gkportal.validation.ValidPhoneNumber;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@Data
@PasswordsNotEqual(passwordFieldName = "password", passwordVerificationFieldName = "matchingPassword", message = "Значения полей пароль и пароль подтверждение должны совпадать")
public class SystemAccount {
    @NotNull(message = "Поле обязательно")
    @Size(min = 2, max = 25, message = "2-5 символов")
    private String firstName;

    @NotNull(message = "Поле обязательно")
    @Size(min = 2, max = 25, message = "2-5 символа")
    private String lastName;


    @NotNull(message = "Поле обязательно")
    @Size(min = 0, max = 25, message = "от 0 до 25 символов")
    private String middleName;

    @NotNull(message = "Поле обязательно")
    @Size(min = 6, max = 25, message = "6-25 символов")
    private String password;

    @NotNull(message = "Поле обязательно")
    @Size(min = 6, max = 25, message = "6-25 символов")
    private String matchingPassword;

    @Email(message = "Почта указана не корректна")
    @NotNull(message = "Поле обязательно")
    @Size(min = 5, max = 25, message = "5-25 символов")
    private String email;

    @ValidPhoneNumber(message = "Телефон указан не корректно")
    @NotNull(message = "Поле обязательно")
    @Size(min = 10, message = "Минимум 10 символов")
    private String phoneNumber;

    List<FlatRegDTO> flats = new ArrayList<>();

    private ContactType contactType;
    //private Boolean boughtParkingPlace;

    private Boolean allowContactsSharing; // Пока бесполезная фигня, но потом прикрутим.

    //private List<String> interestedIn;

    //private String extraInterests;

    //private Integer carsInFamily;

   // private String comments;




    public SystemAccount() {

    }
}
