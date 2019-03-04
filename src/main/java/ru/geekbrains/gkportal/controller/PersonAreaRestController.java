package ru.geekbrains.gkportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.gkportal.dto.FilterUserProfileFlat;
import ru.geekbrains.gkportal.dto.FlatDTO;
import ru.geekbrains.gkportal.entity.Account;
import ru.geekbrains.gkportal.entity.Communication;
import ru.geekbrains.gkportal.entity.Contact;
import ru.geekbrains.gkportal.entity.Flat;
import ru.geekbrains.gkportal.repository.AccountRepository;
import ru.geekbrains.gkportal.security.IsAuthenticated;
import ru.geekbrains.gkportal.service.AuthenticateService;
import ru.geekbrains.gkportal.service.FlatsService;
import ru.geekbrains.gkportal.service.MailService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/rest/lk")
public class PersonAreaRestController {

    private AccountRepository accountRepository;
    private AuthenticateService authenticateService;
    private FlatsService flatsService;

    private MailService mailService;

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }


    @Autowired
    public void setAuthenticateService(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setFlatsService(FlatsService flatsService) {
        this.flatsService = flatsService;
    }

    @IsAuthenticated
    @RequestMapping(value = "/neighbors-flats/{filterTypeId}", method = RequestMethod.GET)
    public List<FlatDTO> showNeighbors(@PathVariable(name = "filterTypeId") int filterId) {

        Account account = accountRepository.findOneByLogin(authenticateService.getCurrentUser().getUsername());
        if (account == null) {
            return null;
        }

        Contact contact = account.getContact();
        Collection<Flat> flats = contact.getFlats();


        List<FlatDTO> flatList = new ArrayList<>();

        for (Flat flat : flats) {

            switch (filterId) {
                case FilterUserProfileFlat.FLOOR:
                    flatList.addAll(flatsService.getFlatContactsByHouseByPorhByFloor(flat.getHouse(),
                            flat.getPorch(), flat.getFloor(), flat.getAccountCount(), flat.getFlatNumber()));
                    break;

                case FilterUserProfileFlat.RISER:
                    flatList.addAll(flatsService.getFlatContactByHouseByRiser(flat.getHouse(),
                            flat.getRiser(), flat.getAccountCount(), flat.getFlatNumber()));
                    break;

                case FilterUserProfileFlat.PORCH:
                    flatList.addAll(flatsService.getFlatContactsByHouseByPorh(flat.getHouse(),
                            flat.getPorch(), flat.getAccountCount(), flat.getFlatNumber()));
                    break;

                case FilterUserProfileFlat.HOUSE:
                    flatList.addAll(flatsService.getFlatContactsByHouse(flat.getHouse(),
                            flat.getAccountCount(), flat.getFlatNumber()));
                    break;
            }

        }

        return flatList;
    }


    @IsAuthenticated
    @PostMapping("/neighbors-flats/selected")
    public int showTestFlats(HttpServletRequest httpServletRequest, @RequestParam(name = "ids") String ids,
                             @RequestParam(name = "msg") String msg) {

        if (ids == null || msg == null ||
                ids.isEmpty() || msg.isEmpty()) {

            return HttpStatus.BAD_REQUEST.value();
        }

        String[] idFlatArray = ids.split(",");


        for (String id : idFlatArray) {

            Flat flat = flatsService.getById(id);
            Collection<Contact> contacts = flat.getContacts();

            for (Contact contact : contacts) {

                Collection<Communication> communications = contact.getCommunications();

                for (Communication communication : communications) {

                    if (communication.getCommunicationType().getDescription().equals("Email")) {

                        mailService.sendMail(communication.getIdentify(), "Сообщение от соседей ЖК Город", msg, true);
                        System.out.println(communication.getIdentify() + " " + msg);
                    }
                }
            }
        }

        return HttpStatus.OK.value();
    }

}