package ru.geekbrains.gkportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.geekbrains.gkportal.dto.*;
import ru.geekbrains.gkportal.entity.Contact;
import ru.geekbrains.gkportal.entity.questionnaire.Question;
import ru.geekbrains.gkportal.entity.questionnaire.Questionnaire;
import ru.geekbrains.gkportal.service.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;

@Controller
public class RegistrationController {

    private HouseService houseService;
    private ContactTypeService contactTypeService;
    private AccountService accountService;
    private ContactService contactService;
    private CommunicationService communicationService;
    private QuestionnaireService questionnaireService;
    private OwnershipTypeService ownershipTypeService;
    private AnswerResultService answerResultService;

    @Autowired
    public void setAnswerResultService(AnswerResultService answerResultService) {
        this.answerResultService = answerResultService;
    }

    @Autowired
    public void setOwnershipTypeService(OwnershipTypeService ownershipTypeService) {
        this.ownershipTypeService = ownershipTypeService;
    }

    @Autowired
    public void setCommunicationService(CommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    @Autowired
    public void setQuestionnaireService(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
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
        account.setContactType(contactTypeService.getContactTypeByDescription(ContactTypeService.OWNER_TYPE));
        account.getFlats().add(new FlatRegDTO());
        //model.addAttribute("flat", new FlatRegDTO());
        model.addAttribute("systemUser", account);
        List<String> housingList = houseService.getHousingNumList();

        //housingList.add(0, "");
        model.addAttribute("housingList", housingList);
        model.addAttribute("userTypes", contactTypeService.getAllContactTypes());
        return "reg-form";
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


    @GetMapping("/regQuestion")
    public String regQuestion(Model model) {
        SystemAccountToOwnerShip account = new SystemAccountToOwnerShip();
        account.getOwnerships().add(new OwnershipRegDTO());
        account.setContactType(contactTypeService.getContactTypeByDescription(ContactTypeService.OWNER_TYPE));
        //model.addAttribute("flat", new FlatRegDTO());

        //List<String> housingList = houseService.getHousingNumList();

        //housingList.add(0, "");
        //model.addAttribute("housingList", housingList);

        Questionnaire questionnaire = putQuestionnaireToModel(model);
        if (questionnaire != null) {
            AnswerResultDTO form = new AnswerResultDTO(questionnaire.getQuestions(), questionnaire.getUuid());
            account.setAnswerResultDTO(form);
        }
        model.addAttribute("systemUser", account);

        model.addAttribute("ownershipTypes", ownershipTypeService.getAllOwnershipTypesByIsUseInQuestionnaire());

        return "reg-question-form";
    }


    @PostMapping(value = "/userQuestionRegister")
    public String registerQuestionUser(@Valid @ModelAttribute("systemUser") SystemAccountToOwnerShip systemAccount,
                                       BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            createErrorModel(systemAccount, model, "Не все поля заполнены правильно!");
            return "reg-question-form";
        }

        if (contactService.isContactExist(systemAccount)){
            // TODO: 09.02.2019  email to @chertenokru
            createErrorModel(systemAccount, model, "Вы уже ответили на анкету. " +
                    "Если в первой анкети вы не указали один из объектов недвижемости или допустили ошибку, " +
                    "свяжитесь с администратором системы Владимир (+7...). Мы удалим все ваши ответы, " +
                    "и вы сможите ответить заново" +
                    "А если в нашем ЖК появилось два тезки, то наша система пока ни такая умная, но мы работаем над этим");

            return "reg-question-form";
        }
        if (accountService.isLoginExist(systemAccount.getEmail())) {
            createErrorModel(systemAccount, model, "Указанный логин уже существует");
            return "reg-question-form";
        }

        try {

            Contact contact = contactService.getOrCreateContact(systemAccount);
            contactService.save(contact);
            answerResultService.saveAnswerResultDTO(systemAccount.getAnswerResultDTO(), contact);


            return "reg-success";
        } catch (Throwable throwable) {
            throwable.printStackTrace(); // TODO: 02.02.2019 to Log
            createErrorModel(systemAccount, model, "Произошла непредвиденная ошибка");
            return "reg-question-form";
        }
    }


    private Questionnaire putQuestionnaireToModel(Model model) {
        Questionnaire questionnaire;
        String questionnaireId = "bb2248ae-2d7e-427d-85ef-7b85888f0319";

        if ((questionnaire = questionnaireService.findById(questionnaireId)) == null) {
            model.addAttribute("notFoundNumber", questionnaireId);
            model.addAttribute("questionnaireList", questionnaireService.findAll());
            return null;
        }
        questionnaire.getQuestions().sort(Comparator.comparingInt(Question::getSortNumber));
        model.addAttribute("questionnaire", questionnaire);
        return questionnaire;
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

    private void createErrorModel(SystemAccountToOwnerShip systemAccount, Model model, String error) {

        model.addAttribute("ownershipTypes", ownershipTypeService.getAllOwnershipTypes());
        model.addAttribute("registrationError", error);
        Questionnaire questionnaire = putQuestionnaireToModel(model);


    }



   /* @ModelAttribute("interests")
    public String[] getMultiCheckboxAllValues() {
        return new String[] {
                "Место в паркинге", "Детский сад", "Школа"
        };
    }*/
}
