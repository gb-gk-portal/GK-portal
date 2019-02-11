package ru.geekbrains.gkportal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.geekbrains.gkportal.entity.ContactType;
import ru.geekbrains.gkportal.validation.PasswordsNotEqual;
import ru.geekbrains.gkportal.validation.ValidPhoneNumber;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PasswordsNotEqual(passwordFieldName = "password", passwordVerificationFieldName = "matchingPassword", message = "Значения полей пароль и пароль подтверждение должны совпадать")
public class SystemAccount implements SystemAccountDTO {

    @Size(
            min = 3,
            max = 100,
            message = "Имя ${validatedValue} должно быть от {min} до {max} символов."
    )
    @NotBlank(message = "Имя не может быть пустным.")
    private String firstName;

    @Size(
            max = 25,
            message = "Отчество ${validatedValue} должно быть меньше {max} символов."
    )
    private String middleName;

    @Size(
            min = 3,
            max = 25,
            message = "Фамилия ${validatedValue} должна быть от {min} до {max} символов."
    )
    @NotBlank(message = "Фамилия не может быть пустной.")
    private String lastName;


    @Size(
            min = 6,
            max = 25,
            message = "Пароль ${validatedValue} должен быть от {min} до {max} символов."
    )
    @NotBlank(message = "Пароль не может быть пустной.")
    private String password;

    @Size(
            min = 6,
            max = 25,
            message = "Пароль ${validatedValue} должен быть от {min} до {max} символов."
    )
    @NotBlank(message = "Пароль не может быть пустной.")
//    @PasswordsNotEqual //TODO: Точно ли нам это надо на класс ставить?
    private String matchingPassword;

    @Email(message = "Почта указана не корректна")
    //TODO: IMHO not null и size тут лишнее, аннотация @Email всё сделает
//    @NotNull(message = "Поле обязательно")
//    @Size(min = 5, max = 25, message = "5-25 символов")
    private String email;

    @Size(
            min = 10,
            message = "Телефон ${validatedValue} должен состоять минимум из {min} символов в формате - 9161234567."
    )
    @NotBlank(message = "Телефон не может быть пустной.")
    @ValidPhoneNumber(message = "Телефон указан не корректно")
    private String phoneNumber;

//    @Valid
    List<FlatRegDTO> flats = new ArrayList<>();

//    @Valid
    private ContactType contactType;
    //private Boolean boughtParkingPlace;

    private Boolean allowContactsSharing; // Пока бесполезная фигня, но потом прикрутим.

    //private List<String> interestedIn;

    //private String extraInterests;

    //private Integer carsInFamily;

   // private String comments;

}
