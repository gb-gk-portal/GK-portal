package ru.geekbrains.gkportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.geekbrains.gkportal.entity.Account;
import ru.geekbrains.gkportal.entity.Communication;
import ru.geekbrains.gkportal.entity.Contact;
import ru.geekbrains.gkportal.entity.Flat;
import ru.geekbrains.gkportal.repository.AccountRepository;
import ru.geekbrains.gkportal.repository.CommunicationRepository;
import ru.geekbrains.gkportal.repository.FlatRepository;
import ru.geekbrains.gkportal.service.AuthenticateService;

import java.util.Collection;



@Controller
public class PersonAreaController {

    private AccountRepository accountRepository;
    private AuthenticateService authenticateService;

    @Autowired
    public void setAuthenticateService(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
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
                    model.addAttribute("contact", contact);
                    model.addAttribute("communications", communications);
                    model.addAttribute("flats", flats);
                    return "lk";
                }
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
}
