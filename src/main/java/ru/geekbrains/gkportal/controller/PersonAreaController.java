package ru.geekbrains.gkportal.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.geekbrains.gkportal.dto.FilterUserProfileFlat;
import ru.geekbrains.gkportal.dto.Porch;
import ru.geekbrains.gkportal.dto.QuestionnaireContactResult;
import ru.geekbrains.gkportal.entity.*;
import ru.geekbrains.gkportal.repository.AccountRepository;
import ru.geekbrains.gkportal.security.IsAuthenticated;
import ru.geekbrains.gkportal.service.AnswerResultService;
import ru.geekbrains.gkportal.service.AuthenticateService;
import ru.geekbrains.gkportal.service.FlatsService;
import ru.geekbrains.gkportal.service.HouseService;

import java.util.*;

import static ru.geekbrains.gkportal.config.TemplateNameConst.*;


@Controller
public class PersonAreaController {

    private static final Logger logger = Logger.getLogger(PersonAreaController.class);

    private AccountRepository accountRepository;
    private AuthenticateService authenticateService;
    private AnswerResultService answerResultService;
    private HouseService houseService;
    private FlatsService flatsService;

    @Autowired
    public void setAuthenticateService(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @Autowired
    public void setHouseService(HouseService houseService) {
        this.houseService = houseService;
    }

    @Autowired
    public void setFlatsService(FlatsService flatsService) {
        this.flatsService = flatsService;
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
                    model.addAttribute("ownerships", ownerships);
                    return returnShablon(model, LK_MAIN_FORM);
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("404");
            }
            return "404";
        } else {
            return returnShablon(model, LOGIN_FORM);
        }
    }

    @GetMapping("/lk/{login}")
    //todo fix it можно адаптировать под палень для отображение информации администратору
    public String personArea(@PathVariable(name = "login") String login, Model model) {
        Account account = accountRepository.findOneByLogin(authenticateService.getCurrentUser().getUsername());
        if (account != null && !account.isActive()) {
            return returnShablon(model, LOGIN_FORM);
        }
        Contact contact = account.getContact();
        model.addAttribute("contact", contact);
        return returnShablon(model, LK_MAIN_FORM);
    }

    @GetMapping("/lk/questionnaire-answer-result")
    @IsAuthenticated
    public String showAnswerResult(Model model) {
        Account account = accountRepository.findOneByLogin(authenticateService.getCurrentUser().getUsername());
        if (account == null) {
            return returnShablon(model, LOGIN_FORM);
        }

//        if (account != null && !account.isActive()) {
//            return "redirect:/login";
//        }

        Contact contact = account.getContact();

        List<QuestionnaireContactResult> questionnaireContactResultList = answerResultService.getAllByContact(contact);

        model.addAttribute("questionnaireContactResultList", questionnaireContactResultList);

        return returnShablon(model, LK_QUESTIONNAIRE_RESULT);
    }


    @IsAuthenticated
    @GetMapping("/lk/neighbors-message")
    public String showNeighborsMessagePage(Model model) {

        Account account = accountRepository.findOneByLogin(authenticateService.getCurrentUser().getUsername());
        if (account == null) {
            return "redirect:/login";
        }

        Map<Integer, String> filterTypeMap = new HashMap<>();
        filterTypeMap.put(FilterUserProfileFlat.FLOOR, "Соседи по этажу");

        if (account.getRoles().stream().anyMatch(x -> x.getDescription().equals("admin"))) {
            filterTypeMap.put(FilterUserProfileFlat.RISER, "Соседи по стояку");
            filterTypeMap.put(FilterUserProfileFlat.PORCH, "Соседи по подъезду");
            filterTypeMap.put(FilterUserProfileFlat.HOUSE, "Соседи по дому");
        }

        model.addAttribute("filterTypeMap", filterTypeMap);

        return returnShablon(model, LK_NEIGHBORS_MESSAGE_FORM);
    }

    @IsAuthenticated
    @GetMapping("/lk/show-flats")
    public String showFlats(Model model) {
        if (authenticateService.isCurrentUserAuthenticated()) {
            Account account = accountRepository.findOneByLogin(authenticateService.getCurrentUser().getUsername());
            if (account != null) {
                Contact contact = account.getContact();
                if (contact != null) {
                    Collection<Flat> flats = contact.getFlats();
                    List<Porch> porches = new ArrayList<>();
                    for (Flat flat:flats) {
                        Porch porch = houseService.build(flat.getHouse(), flat.getPorch());
                        // временно, надо список уникальных значений дом, подъезд
                        if (!porches.contains(porch))
                            porches.add(porch);
                    }
//                    Porch porch = houseService.build(2, 1);
                    model.addAttribute("porches", porches);
                    model.addAttribute("showType", "post");
                    return returnShablon(model, LK_SHOW_FLATS);
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("404");
            }
            return "404";
        } else {
            return returnShablon(model, LOGIN_FORM);
        }
    }

}
