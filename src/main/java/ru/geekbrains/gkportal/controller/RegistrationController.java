package ru.geekbrains.gkportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.gkportal.dto.*;
import ru.geekbrains.gkportal.entity.Contact;
import ru.geekbrains.gkportal.entity.questionnaire.Question;
import ru.geekbrains.gkportal.entity.questionnaire.Questionnaire;
import ru.geekbrains.gkportal.service.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Controller
public class RegistrationController {

    private final static String QUESTIONNAIRE_ID = "bb2248ae-2d7e-427d-85ef-7b85888f0319";
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
    public String regQuestion(@RequestParam(name = "uuid", required = false) String uuid, Model model, HttpSession session) {
        SystemAccountToOwnerShip account = null;
        Questionnaire questionnaire = putQuestionnaireToModel(model);

        if (uuid != null) {
            account = (SystemAccountToOwnerShip) session.getAttribute("systemUser");
            if (account != null) {
                if (!account.getUuid().equals(uuid)) {
                    account = null;
                } else {
                    account.setEmail(null);
                    account.setPhoneNumber(null);
                    account.setFirstName(null);
                    account.setMiddleName(null);
                    account.setLastName(null);
                }
            }
        }
        if (account == null) {
            account = new SystemAccountToOwnerShip();
            account.getOwnerships().add(new OwnershipRegDTO());
            account.setContactType(contactTypeService.getContactTypeByDescription(ContactTypeService.OWNER_TYPE));
            //model.addAttribute("flat", new FlatRegDTO());
            //List<String> housingList = houseService.getHousingNumList();
            //housingList.add(0, "");
            //model.addAttribute("housingList", housingList);
            if (questionnaire != null) {
                AnswerResultDTO form = new AnswerResultDTO(questionnaire.getQuestions(), questionnaire.getUuid());
                account.setAnswerResultDTO(form);
            }
        }

        model.addAttribute("systemUser", account);
        putOwnershipTypes(model);

        return "reg-question-form";
    }


    @PostMapping(value = "/userQuestionRegister")
    public String registerQuestionUser(@Valid @ModelAttribute("systemUser") SystemAccountToOwnerShip systemAccount,

                                       BindingResult bindingResult, Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            createErrorModel(systemAccount, model, "Не все поля заполнены правильно!");
            return "reg-question-form";
        }

        Contact contact = contactService.getContact(systemAccount);
        if (contact != null) {
            if (questionnaireService.isQuestionnaireContactExist(
                    questionnaireService.findByIdAndSortAnswers(systemAccount.getAnswerResultDTO().getQuestionnaireId()),
                    contact)) {
                // TODO: 09.02.2019  email to @chertenokru
                createErrorModel(systemAccount, model, "Вы уже ответили на анкету. " +
                        "Если в первой анкети вы не указали один из объектов недвижемости или допустили ошибку, " +
                        "свяжитесь с администратором системы Владимир (+7 (916) 197-32-36, телеграм - @chertenokru)." +
                        " Мы удалим все ваши ответы, и вы сможите ответить заново" +
                        "А если в нашем ЖК появилось два полных тезки, то наша система пока ни такая умная, тоже ссобщите по контактам выше");

                return "reg-question-form";
            }
        }
        if (accountService.isLoginExist(systemAccount.getEmail())) {
            createErrorModel(systemAccount, model, "Указанный логин уже существует");
            return "reg-question-form";
        }

        try {
            if (contact == null) contact = contactService.getOrCreateContact(systemAccount);
            answerResultService.saveAnswerResultDTO(systemAccount.getAnswerResultDTO(), contactService.save(contact));
            systemAccount.setUuid(UUID.randomUUID().toString());
            session.setAttribute("systemUser", systemAccount);
            model.addAttribute("uuid", systemAccount.getUuid());
            return "reg-success";
        } catch (Throwable throwable) {
            throwable.printStackTrace(); // TODO: 02.02.2019 to Log
            createErrorModel(systemAccount, model, "Произошла непредвиденная ошибка. Обновите страницу и попробуйте снова");
            return "reg-question-form";
        }
    }

//    @GetMapping("reg-success")
//    public String registerSuccess(@Valid @ModelAttribute("systemUser") SystemAccountToOwnerShip systemAccount,
//                                  BindingResult bindingResult, Model model) {
//        System.out.println("stop");
//        return "reg-success";
//    }

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
        putOwnershipTypes(model);
        model.addAttribute("registrationError", error);
        putQuestionnaireToModel(model);
    }

//    private void createModel(SystemAccountToOwnerShip systemAccount, Model model) {
//        putOwnershipTypes(model);
//        putQuestionnaireToModel(model);
//    }

    private void putOwnershipTypes(Model model) {
        model.addAttribute("ownershipTypes", ownershipTypeService.getAllOwnershipTypes());
    }

    private Questionnaire putQuestionnaireToModel(Model model) {
        Questionnaire questionnaire;
        String questionnaireId = QUESTIONNAIRE_ID;

        if ((questionnaire = questionnaireService.findByIdAndSortAnswers(questionnaireId)) == null) {
            model.addAttribute("notFoundNumber", questionnaireId);
            model.addAttribute("questionnaireList", questionnaireService.findAll());
            return null;
        }
        questionnaire.getQuestions().sort(Comparator.comparingInt(Question::getSortNumber));
        model.addAttribute("questionnaire", questionnaire);
        return questionnaire;
    }

   /* @ModelAttribute("interests")
    public String[] getMultiCheckboxAllValues() {
        return new String[] {
                "Место в паркинге", "Детский сад", "Школа"
        };
    }*/
}
