package ru.geekbrains.gkportal.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.geekbrains.gkportal.dto.QuestionnaireContactResult;
import ru.geekbrains.gkportal.entity.Account;
import ru.geekbrains.gkportal.entity.Communication;
import ru.geekbrains.gkportal.entity.Contact;
import ru.geekbrains.gkportal.entity.Flat;
import ru.geekbrains.gkportal.entity.Ownership;
import ru.geekbrains.gkportal.repository.AccountRepository;
import ru.geekbrains.gkportal.security.IsAuthenticated;
import ru.geekbrains.gkportal.service.AnswerResultService;
import ru.geekbrains.gkportal.service.AuthenticateService;

import java.util.Collection;
import java.util.List;


@Controller
public class PersonAreaController {

    private static final Logger logger = Logger.getLogger(PersonAreaController.class);

    private AccountRepository accountRepository;
    private AuthenticateService authenticateService;
    private AnswerResultService answerResultService;

    @Autowired
    public void setAuthenticateService(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setAnswerResultService(AnswerResultService answerResultService) {
        this.answerResultService = answerResultService;
    }

    @GetMapping("/user/profile")
    public String showProfile(Model model) {
        if (authenticateService.isCurrentUserAuthenticated()) {
            Account account = accountRepository.findOneByLogin(authenticateService.getCurrentUser().getUsername());
            if (account != null) {
                Contact contact = account.getContact();
                if (contact != null) {
                    Collection<Communication> communications = contact.getCommunications();
                    Collection<Flat> flats = contact.getFlats();
                    Collection<Ownership> ownerships = contact.getOwnerships();
                    model.addAttribute("contact", contact);
                    model.addAttribute("communications", communications);
                    model.addAttribute("flats", flats);
                    model.addAttribute("ownerships",ownerships);
                    return "lk";
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("404");
            }
            return "404";
        } else {
            return "login";
        }
    }

    @GetMapping("/lk/{login}")
    //todo fix it можно адаптировать под палень для отображение информации администратору
    public String personArea(@PathVariable(name = "login") String login, Model model) {
        Account account = accountRepository.findOneByLogin(login);
        if (account != null && !account.isActive()) {
            return "login";
        }
        Contact contact = account.getContact();
        model.addAttribute("contact", contact);
        return "lk";
    }

    @GetMapping("/lk/questionnaire-answer-result")
    @IsAuthenticated
    public String showAnswerResult(Model model) {
        Account account = accountRepository.findOneByLogin(authenticateService.getCurrentUser().getUsername());
        if (account == null) {
            return "redirect:/login";
        }

//        if (account != null && !account.isActive()) {
//            return "redirect:/login";
//        }

        Contact contact = account.getContact();

        List<QuestionnaireContactResult> questionnaireContactResultList = answerResultService.getAllByContact(contact);

        model.addAttribute("questionnaireContactResultList", questionnaireContactResultList);

        return "lk-questionnaire-answer-result";
    }
}
