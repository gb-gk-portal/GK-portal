package ru.geekbrains.gkportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.geekbrains.gkportal.dto.FlatRegDTO;
import ru.geekbrains.gkportal.dto.Porch;
import ru.geekbrains.gkportal.dto.SystemAccount;
import ru.geekbrains.gkportal.entity.Contact;
import ru.geekbrains.gkportal.service.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class RegistrationController {

    private HouseService houseService;
    private ContactTypeService contactTypeService;
    private AccountService accountService;
    private ContactService contactService;
    private CommunicationService communicationService;

    @Autowired
    public void setCommunicationService(CommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    @Autowired
    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setContactTypeService(ContactTypeService contactTypeService) {
        this.contactTypeService = contactTypeService;
    }

    @Autowired
    public void setHouseService(HouseService houseService) {
        this.houseService = houseService;
    }

    @GetMapping("/reg")
    public String reg(Model model) {
        SystemAccount account = new SystemAccount();
        account.getFlats().add(new FlatRegDTO());
        //model.addAttribute("flat", new FlatRegDTO());
        model.addAttribute("systemUser", account);
        List<String> housingList = houseService.getHousingNumList();

        //housingList.add(0, "");
        model.addAttribute("housingList", housingList);
        model.addAttribute("userTypes", contactTypeService.getAllContactTypes());
        return "reg-form";
    }

    @GetMapping("/showPorch/{build}/{porch}")
    public String showPorch(@PathVariable(name = "build") int build, @PathVariable(name = "porch") int porchNum, Model model) {
        //todo проверка что юзер зарегистрирован и имеет права на данное действие (надо подумать)
        Porch porch = houseService.build(build, porchNum);
        model.addAttribute("porch", porch);
        return "porch-form";
    }

    /*@GetMapping("/getPorchCount/{build}")
    public String getPorchCount(@ModelAttribute("systemUser") SystemAccount systemAccount, @PathVariable(name = "build") int build, Model model) {
        List<String> porchList = new ArrayList<>();
        porchList.add("");
        int count = houseService.getHousingPorchCount(build);
        for (int i = 1; i <= count; i++) porchList.add(String.valueOf(i));
        model.addAttribute("porchList", porchList);
        return "select-porch-form";
    }*/

    @GetMapping("/confirmMail/{mail}/{code}")
    public String confirmMail(@PathVariable(name = "code") String code, @PathVariable(name = "mail") String mail, Model model) {
        Contact contact = communicationService.confirmAccountAndGetContact(mail, code);
        boolean confirm = false;
        if (contact != null) {
            accountService.confirmAccount(contact);
            confirm = true;
        }
        model.addAttribute("resultString", confirm ? "Подзравляю, Ваш аккаунт подтверждён!" : "Не удалось подтвердить емайл, попробуйте повторить!");
        return "confirm-mail";
    }

    @PostMapping(value = "/userRegister")
    public String registerUser(@Valid @ModelAttribute("systemUser") SystemAccount systemAccount, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            createErrorModel(systemAccount, model, "Не все поля заполнены правильно!");
            return "reg-form";
        }

        if (accountService.isLoginExist(systemAccount.getEmail())) {
            createErrorModel(systemAccount, model, "Указанный логин уже существует");
            return "reg-form";
        }

        try {
            accountService.createAccount(systemAccount);
            return "reg-success";
        } catch (Throwable throwable) {
            throwable.printStackTrace(); // TODO: 02.02.2019 to Log
            createErrorModel(systemAccount, model, "Произошла непредвиденная ошибка");
            return "reg-form";
        }


    }

    private void createErrorModel(SystemAccount systemAccount, Model model, String error) {
        //House house = houseService.build(systemAccount.getHousingNumber());
        List<String> housingList = houseService.getHousingNumList();
        //housingList.add(0, "");
        model.addAttribute("housingList", housingList);
        model.addAttribute("houseService", houseService);
       /* if (systemAccount.getHousingNumber() != null && systemAccount.getHousingNumber() != 0) {
            List<String> porchList = new ArrayList<>();
            porchList.add("");
            int count = houseService.getHousingPorchCount(systemAccount.getHousingNumber());
            for (int i = 1; i <= count; i++) porchList.add(String.valueOf(i));
            model.addAttribute("porchList", porchList);

        }*/


        model.addAttribute("userTypes", contactTypeService.getAllContactTypes());
        model.addAttribute("registrationError", error);
    }

   /* @ModelAttribute("interests")
    public String[] getMultiCheckboxAllValues() {
        return new String[] {
                "Место в паркинге", "Детский сад", "Школа"
        };
    }*/
}
