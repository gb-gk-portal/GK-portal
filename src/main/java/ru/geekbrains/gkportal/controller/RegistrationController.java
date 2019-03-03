package ru.geekbrains.gkportal.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.gkportal.dto.*;
import ru.geekbrains.gkportal.entity.Account;
import ru.geekbrains.gkportal.entity.Contact;
import ru.geekbrains.gkportal.entity.questionnaire.Question;
import ru.geekbrains.gkportal.entity.questionnaire.Questionnaire;
import ru.geekbrains.gkportal.entity.questionnaire.QuestionnaireContactConfirm;
import ru.geekbrains.gkportal.service.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static ru.geekbrains.gkportal.config.TemplateNameConst.*;
import static ru.geekbrains.gkportal.service.ContactTypeService.OWNER_TYPE;

@Controller
public class RegistrationController {

    private static final Logger logger = Logger.getLogger(RegistrationController.class);

    private final static String QUESTIONNAIRE_ID = "bb2248ae-2d7e-427d-85ef-7b85888f0319";
    private HouseService houseService;
    private ContactTypeService contactTypeService;
    private AccountService accountService;
    private ContactService contactService;
    private CommunicationService communicationService;
    private QuestionnaireService questionnaireService;
    private OwnershipTypeService ownershipTypeService;
    private OwnershipService ownershipService;
    private AnswerResultService answerResultService;
    private MailService mailService;

    @Autowired
    public void setOwnershipService(OwnershipService ownershipService) {
        this.ownershipService = ownershipService;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

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

    /**
     * Начальная форма регистрации пользователя
     *
     * @param model
     * @return
     */
    @GetMapping("/reg")
    public String reg(Model model, @RequestParam(name = "fast", required = false) Boolean fast) {
        if (fast == null) fast = false;

        SystemAccount account = new SystemAccount();
        account.setFastRegistration(fast);

        // по умолчанию собственник
        account.setContactType(contactTypeService.getContactTypeByDescription(OWNER_TYPE));
        account.getFlats().add(new FlatRegDTO());
        model.addAttribute("systemUser", account);
        model.addAttribute("housingList", houseService.getHousingNumList());
        model.addAttribute("userTypes", contactTypeService.getAllContactTypes());


        return returnShablon(model, fast ? REGISTRATION_FAST_FORM : REGISTRATION_FULL_FORM);
    }


    /**
     * Регистрация пользователя,
     * обработка заполнения полей
     *
     * @param systemAccount служебный класс с полями регистрации
     * @param bindingResult результат валидации
     * @param model
     * @return
     */
    @PostMapping(value = "/userRegister")
    public String registerUser(@Valid @ModelAttribute("systemUser") SystemAccount systemAccount, BindingResult bindingResult, Model model) {

        if (systemAccount.getFastRegistration() == null) systemAccount.setFastRegistration(false);
        String returnFailShablon = systemAccount.getFastRegistration() ? REGISTRATION_FAST_FORM : REGISTRATION_FULL_FORM;
        StringBuilder errorText = new StringBuilder();
        boolean isError = false;

        // быстрая регистрация, только 3 ошибки из валидации + свои ошибки
        if (systemAccount.getFastRegistration()) {

            if (bindingResult.hasFieldErrors("email") || bindingResult.hasFieldErrors("password") ||
                    bindingResult.hasFieldErrors("matchingPassword")) {
                isError = true;
            }

            if (accountService.isLoginExist(systemAccount.getEmail())) {
                errorText.append("Эта почта уже использована для регистрации, пройдите полную регистрацию");
                isError = true;
            }

            Collection<Contact> contactList = null;
            try {
                contactList = contactService.getContaсtListByEmail(systemAccount.getEmail());
            } catch (Throwable throwable) {
                errorText.append("Ошибка при поиске контактов по этой почте, пройдите полную регистрацию");
                isError = true;
            }

            if (contactList == null || contactList.isEmpty()) {
                errorText.append("Не найдено контактов по этой почте, пройдите полную регистрацию");
                isError = true;
            }

            if (!isError) {
                try {
                    // создаём аккаунт
                    //todo если несколько аккаунтов то нужна дополнительная форма выборв
                    Account account = accountService.createAccount(systemAccount, contactList.stream().findFirst().get());
                    // отправляем подтверждающее письмо
                    mailService.sendRegistrationMail(account.getContact());
                    return returnShablon(model, REGISTRATION_SUCCESS_FROM);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    errorText.append("Произошла непредвиденная ошибка");
                    logger.error(errorText, throwable);
                }
            }

            // ошибка при быстрой регистрации
            model.addAttribute("registrationError", errorText.toString());
            logger.info(errorText);
            return returnShablon(model, returnFailShablon);
        } // конец блока быстрой регистрации


        // полная регистрация
        if (bindingResult.hasErrors()) {
            isError = true;
            errorText.append("Не все поля заполнены правильно!");
        }
        if (accountService.isLoginExist(systemAccount.getEmail())) {
            isError = true;
            errorText.append("Указанный логин уже существует");
        }

        if (!isError) {
            try {
                // создаём аккаунт
                Account account = accountService.createAccount(systemAccount);
                // отправляем подтверждающее письмо
                mailService.sendRegistrationMail(account.getContact());

                return returnShablon(model, REGISTRATION_SUCCESS_FROM);
            } catch (Throwable throwable) {
                throwable.printStackTrace(); // TODO: 02.02.2019 to Log
                errorText.append("Произошла непредвиденная ошибка");
                logger.error(errorText, throwable);
            }
        }

        List<String> housingList = houseService.getHousingNumList();
        model.addAttribute("housingList", housingList);
        model.addAttribute("houseService", houseService);
        model.addAttribute("userTypes", contactTypeService.getAllContactTypes());
        model.addAttribute("registrationError", errorText.toString());
        logger.info(errorText);
        return returnShablon(model, returnFailShablon);
    }

    /**
     * Регистрация с опросом, начальная форма
     * если задан uuid юзера,  то данные начальные берём с него
     *
     * @param uuid    guid предыдущего юзера, с которого копируем данные
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/regQuestion")
    public String regQuestion(@RequestParam(name = "uuid", required = false) String uuid, Model model, HttpSession session) {
        SystemAccountToOwnerShip systemAccount = null;
        Questionnaire questionnaire = putQuestionnaireToModel(model, QUESTIONNAIRE_ID);

        if (uuid != null) {
            // используем сохранненные данные из сессиии
            systemAccount = (SystemAccountToOwnerShip) session.getAttribute("systemUser");
            if (systemAccount != null) {
                if (!systemAccount.getUuid().equals(uuid)) {
                    systemAccount = null;
                } else {
                    systemAccount.setEmail(null);
                    systemAccount.setPhoneNumber(null);
                    systemAccount.setFirstName(null);
                    systemAccount.setMiddleName(null);
                    systemAccount.setLastName(null);
                }
            }
        }
        // новый юзер
        if (systemAccount == null) {
            systemAccount = new SystemAccountToOwnerShip();
            systemAccount.getOwnerships().add(new OwnershipRegDTO());
            systemAccount.setContactType(contactTypeService.getContactTypeByDescription(OWNER_TYPE));
            //model.addAttribute("flat", new FlatRegDTO());
            //List<String> housingList = houseService.getHousingNumList();
            //housingList.add(0, "");
            //model.addAttribute("housingList", housingList);
            if (questionnaire != null) {
                AnswerResultDTO form = new AnswerResultDTO(questionnaire.getQuestions(), questionnaire.getUuid());
                systemAccount.setAnswerResultDTO(form);
            }
        }
        model.addAttribute("systemUser", systemAccount);
        putOwnershipTypes(model);
        return returnShablon(model, REGISTRATION_QUESTIONNAIRE_FORM);
    }


    /**
     * Регистрация пользователя с вопросами,
     * обработка полей
     *
     * @param systemAccount
     * @param bindingResult
     * @param model
     * @param session
     * @return
     */
    @PostMapping(value = "/userQuestionRegister")
    public String registerQuestionUser(@Valid @ModelAttribute("systemUser") SystemAccountToOwnerShip systemAccount,
                                       BindingResult bindingResult, Model model, HttpSession session) {

        // заплатка - теряется с формы ?! todo разобраться
        systemAccount.setContactType(contactTypeService.getContactTypeByDescription(OWNER_TYPE));

        StringBuilder errorText = new StringBuilder();
        boolean isError = false;

        // есть ли ошибки
        if (bindingResult.hasErrors() | ownershipService.checkOwnerships(systemAccount.getOwnerships())) {
            errorText.append("Не все поля заполнены правильно!");
            isError = true;
        }

        Contact contact = contactService.getContact(systemAccount);
        // по фио смотрим участвовал ли уже пользователь в опросе
        if (contact != null) {
            if (questionnaireService.isQuestionnaireContactExist(
                    questionnaireService.findByIdAndSortAnswers(systemAccount.getAnswerResultDTO().getQuestionnaireId()),
                    contact)) {
                mailService.sendMailToAdmin("Попытка повторной регистрации с анкетой",
                        "" + contact + " \n почта - " + systemAccount.getEmail() + "\n телефон - " + systemAccount.getPhoneNumber());
                errorText.append("Вы уже ответили на анкету. \n" +
                        "Если в анкете Вы не указали один из объектов недвижемости или допустили ошибку \n" +
                        " или это голосовали не Вы, то свяжитесь с администратором системы \n " +
                        "Владимир (wa - 8 (916) 197-32-36, телеграм - @chertenokru, mail - admin@chertenok.ru)\n" +
                        " Мы удалим все ваши ответы, и вы сможите ответить заново.");
                isError = true;
            }
        }

        if (!isError) {
            // пытаемся создать контакт и сохранить данные
            try {
                contact = contactService.getOrCreateContact(systemAccount, contact);
                answerResultService.saveAnswerResultDTO(systemAccount.getAnswerResultDTO(), contactService.save(contact));
                mailService.sendRegistrationMail(contact, questionnaireService.getQuestionnaireContactConfirm(systemAccount.getAnswerResultDTO().getQuestionnaireId(), contact));
                systemAccount.setUuid(UUID.randomUUID().toString());
                session.setAttribute("systemUser", systemAccount);
                model.addAttribute("uuid", systemAccount.getUuid());
                return returnShablon(model, REGISTRATION_QUESTIONNAIRE_SUCCES_FORM);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                logger.error(errorText, throwable);
                errorText.append("Произошла непредвиденная ошибка. Обновите страницу и попробуйте снова");
            }
        }
        // Есть ошибки
        putOwnershipTypes(model);
        putQuestionnaireToModel(model, QUESTIONNAIRE_ID);
        model.addAttribute("registrationError", errorText.toString());
        logger.info(errorText);
        return returnShablon(model, REGISTRATION_QUESTIONNAIRE_FORM);
    }

    //todo сделать опционным отображение строительного адреса

    /**
     * Возращает визуализацию шахматки подъезда
     *
     * @param build    дом
     * @param porchNum подъезд
     * @param model
     * @return
     */
    @GetMapping("/showPorch/{build}/{porch}")
    public String showPorch(@PathVariable(name = "build") int build, @PathVariable(name = "porch") int porchNum, Model model) {
        //todo проверка что юзер зарегистрирован и имеет права на данное действие (надо подумать)
        Porch porch = houseService.build(build, porchNum);
        model.addAttribute("porch", porch);
        // JS, не надо обёртывать индексом!
        return SCHEME_PORCH_FORM;
    }


    /**
     * Отправляет повторное письмо подтверждение опроса
     * по емайлу
     *
     * @param mail  почтовый адрес
     * @param model
     * @return
     */
    @GetMapping("/getQuestionConfirmMail/{mail}")
    public String getQuestionConfirmMail(@PathVariable(name = "mail") String mail, Model model) {
        boolean result = false;
        boolean questionnaireResultNotFound = true;
        boolean questionnaireResultNotFoundNotConfirm = true;

        String resultText = "Контакты с таким емайлом не найдены";

        Collection<Contact> contaсtList = null;
        // выбираем все контакты с такой почтой и ищем есть ли у них не подтвержденные опросы
        try {
            contaсtList = contactService.getContaсtListByEmail(mail);
            for (Contact contact : contaсtList) {
                QuestionnaireContactConfirm questionnaireContactConfirm = questionnaireService.getQuestionnaireContactConfirm(QUESTIONNAIRE_ID, contact);
                if (questionnaireContactConfirm != null) {
                    questionnaireResultNotFound = false;
                    if (!questionnaireContactConfirm.isConfirmed()) {
                        questionnaireResultNotFoundNotConfirm = false;
                        mailService.sendRegistrationMail(contact, questionnaireContactConfirm);
                    }
                }
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        if (questionnaireResultNotFound) resultText = "Пройденные опросы не найдены";
        if (questionnaireResultNotFoundNotConfirm && !questionnaireResultNotFound)
            resultText = "Все опросы уже подтверждены";
        if (!questionnaireResultNotFoundNotConfirm) resultText = "Подтверждаюшие письма отправлены!";

        model.addAttribute("result", resultText);
        return returnShablon(model, QUESTIONNAIRE_CONFIRM_MAIL);
    }

    /**
     * Обрабатывает ссылку подтверждающую почту при регистрации
     * активирует привязанный аккаунт
     *
     * @param code  код подтверждения
     * @param mail  почта
     * @param model
     * @return
     */
    @GetMapping("/confirmMail/{mail}/{code}")
    public String confirmMail(@PathVariable(name = "code") String code, @PathVariable(name = "mail") String mail, Model model) {

        Contact contact = communicationService.confirmAccountAndGetContact(mail, code, accountService.getContactByLogin(mail));
        boolean confirm = false;
        if (contact != null) {
            accountService.confirmAccount(contact);
            confirm = true;
        }
        model.addAttribute("resultString", confirm ? "Подзравляю, Ваш аккаунт подтверждён!" : "Не удалось подтвердить емайл, попробуйте повторить!");
        logger.info(" Подтверждение аккаунта: " + (confirm ? "успешно" : "нет"));
        return returnShablon(model, REGISTRATION_CONFIRM_MAIL_FORM);
    }


    /**
     * Обрабатывает ссылку подтверждающую результат опроса
     *
     * @param code
     * @param contact_uuid
     * @param model
     * @return
     */
    @GetMapping("/confirmQuestion/{contact_uuid}/{code}")
    public String confirmQuestion(@PathVariable(name = "code") String code, @PathVariable(name = "contact_uuid") String contact_uuid, Model model) {

        Contact contact = contactService.getContactByID(contact_uuid);
        boolean confirm = false;
        if (contact != null) {
            if (questionnaireService.confirmQuetionnaire(contact, code)) {//accountService.confirmAccount(contact);
                confirm = true;
            }
        }
        model.addAttribute("resultString", confirm ? "Поздравляю, Ваш опрос подтверждён!" : "Не удалось подтвердить опрос, попробуйте повторить!");
        logger.info("Подтверждение опроса: " + (confirm ? "Да" : "Нет"));
        return returnShablon(model, REGISTRATION_CONFIRM_MAIL_FORM);
    }


    /**
     * Помещает в модель список типов собственности
     *
     * @param model
     */
    private void putOwnershipTypes(Model model) {
        model.addAttribute("ownershipTypes", ownershipTypeService.getAllOwnershipTypesByIsUseInQuestionnaire());
    }

    /**
     * Размещает в модели опрос и
     * вопросы с ответами
     *
     * @param model
     * @param questionnaireId ID  голосования
     * @return
     */
    private Questionnaire putQuestionnaireToModel(Model model, String questionnaireId) {
        Questionnaire questionnaire;

        if ((questionnaire = questionnaireService.findByIdAndSortAnswers(questionnaireId)) == null) {
            model.addAttribute("notFoundNumber", questionnaireId);
            model.addAttribute("questionnaireList", questionnaireService.findAll());
            return null;
        }
        questionnaire.getQuestions().sort(Comparator.comparingInt(Question::getSortNumber));
        model.addAttribute("questionnaire", questionnaire);
        return questionnaire;
    }


}
