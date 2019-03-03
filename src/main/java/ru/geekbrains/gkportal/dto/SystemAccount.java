package ru.geekbrains.gkportal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.geekbrains.gkportal.dto.interfaces.SystemAccountDTO;
import ru.geekbrains.gkportal.entity.ContactType;
import ru.geekbrains.gkportal.validation.PasswordsNotEqual;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PasswordsNotEqual(
        passwordFieldName = "password",
        passwordVerificationFieldName = "matchingPassword",
        message = "{password.passwordNotEqual}"
)
public class SystemAccount extends AbstractSystemAccount implements SystemAccountDTO {

    @Size(min = 6, max = 25, message = "{password.size}")
    @NotBlank(message = "{password.netBlank}")
    private String password;

    @Size(min = 6, max = 25, message = "{password.size}")
    @NotBlank(message = "{password.netBlank}")
    private String matchingPassword;

    List<FlatRegDTO> flats = new ArrayList<>();

    private ContactType contactType;

    //private Boolean boughtParkingPlace;

    private Boolean allowContactsSharing; // Пока бесполезная фигня, но потом прикрутим.
    private Boolean fastRegistration;

    //private List<String> interestedIn;

    //private String extraInterests;

    //private Integer carsInFamily;

   // private String comments;
}
