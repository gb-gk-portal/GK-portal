package ru.geekbrains.gkportal.service.registration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.geekbrains.gkportal.dto.FlatRegDTO;
import ru.geekbrains.gkportal.dto.SystemAccount;
import ru.geekbrains.gkportal.entity.ContactType;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ValidationSystemAccountTest {

    private Validator validator;
    private SystemAccount systemAccount;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        systemAccount = new SystemAccount();
        systemAccount.setFirstName("Сильвестр");
        systemAccount.setLastName("Сталлоне");
        systemAccount.setMiddleName("Гардэнцио");
        systemAccount.setEmail("sylvesterstalone@gmail.com");
        systemAccount.setPassword("123456");
        systemAccount.setMatchingPassword("123456");
        systemAccount.setPhoneNumber("1234567890");
        systemAccount.setAllowContactsSharing(true);

        ContactType contactType = new ContactType();
        contactType.setDescription("Собственник");
        systemAccount.setContactType(contactType);

        List<FlatRegDTO> flatRegDTOList = new ArrayList<>();
        FlatRegDTO flatRegDTO = new FlatRegDTO();
        flatRegDTO.setFlatNumber(5);
        flatRegDTO.setFloorNumber(1);
        flatRegDTO.setHousingNumber(1);
        flatRegDTO.setPorchNumber(1);
        flatRegDTO.setRoomCount(1);
        flatRegDTOList.add(flatRegDTO);
        systemAccount.setFlats(flatRegDTOList);

    }


    @Test
    public void ValidationSuccessSystemAccountTest() throws Exception {
        Set<ConstraintViolation<SystemAccount>> violations = validator.validate(systemAccount);
        Assert.assertTrue(violations.isEmpty());
        System.out.println(violations);
    }

    @Test
    public void ValidationNameFailedSystemAccountTest() throws Exception {
        systemAccount.setFirstName("");
        Set<ConstraintViolation<SystemAccount>> violations = validator.validate(systemAccount);
        Assert.assertEquals( 3, violations.size());
        // TODO: 21.02.19  до введения {firstName.notBlank} можно было проверять значение выдваемое валидатором, например "Имя  должно быть от 3 до 100 символов."
//        Assert.assertEquals(
//                "{firstName.notBlank}",
//                "{firstName.size}",
//                violations.iterator().next().getMessage()
//        );
    }

    @Test
    public void ValidationEmailFailedSystemAccountTest() throws Exception {
        systemAccount.setEmail("testmail.ru");
        Set<ConstraintViolation<SystemAccount>> violations = validator.validate(systemAccount);
        Assert.assertEquals( 1, violations.size());
        // TODO: 21.02.19  до введения {email.validation} можно было проверять значение выдваемое валидатором, например "Почта указана некорректно"
        Assert.assertEquals(
        "email определен в неверном формате",
        "{email.validation}",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    public void ValidationEmailWithoutDomainFailedSystemAccountTest() throws Exception {
        systemAccount.setEmail("test@mail");
        Set<ConstraintViolation<SystemAccount>> violations = validator.validate(systemAccount);
        Assert.assertEquals( 1, violations.size());
        // TODO: 21.02.19  до введения {email.validation} можно было проверять значение выдваемое валидатором, "Почта указана некорректно"
        Assert.assertEquals(
                "Почта указана некорректно",
                "{email.validation}",
        violations.iterator().next().getMessage()
        );
    }

    @Test
    public void ValidationPhoneFailedSystemAccountTest() throws Exception {
        systemAccount.setPhoneNumber("dcdsf434546");
        Set<ConstraintViolation<SystemAccount>> violations = validator.validate(systemAccount);
        Assert.assertEquals( 1, violations.size());
        // TODO: 21.02.19 до введения {phoneNumber.validPhoneNumber} можно было проверять значение выдваемое валидатором, "Телефон указан некорректно"
        Assert.assertEquals(
                "Телефон указан некорректно",
                "{phoneNumber.validPhoneNumber}",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    public void ValidationPasswordMathFailedSystemAccountTest() throws Exception {
        systemAccount.setPassword("123456");
        systemAccount.setMatchingPassword("654321");
        Set<ConstraintViolation<SystemAccount>> violations = validator.validate(systemAccount);
        // TODO: 21.02.19 до введения {password.passwordNotEqual} можно было проверять значение выдваемое валидатором,
        // например "Значения полей пароль и пароль подтверждение должны совпадать". в данном случае пока не нашёл способа выключать
        Assert.assertEquals( 2, violations.size());
        Assert.assertEquals(
                "Значения полей пароль и пароль подтверждение должны совпадать",
                "{password.passwordNotEqual}",
                violations.iterator().next().getMessage()
        );
    }

    @Test
    public void ValidationEmptyFailedSystemAccountTest() throws Exception {
        systemAccount.setFirstName(null);
        Set<ConstraintViolation<SystemAccount>> violations = validator.validate(systemAccount);
        Assert.assertEquals( 1, violations.size());
        // TODO: 21.02.19 до введения {firstName.notBlank}  можно было проверять значение выдваемое валидатором, например "Имя не может быть пустным."
        Assert.assertEquals(
                "{firstName.notBlank}",
                violations.iterator().next().getMessage()
        );
    }

}

