package ru.geekbrains.gkportal.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.geekbrains.gkportal.entities.Contact;
import ru.geekbrains.gkportal.entities.ContactType;
import ru.geekbrains.gkportal.services.ContactService;
import ru.geekbrains.gkportal.services.ContactTypeService;

@Controller
public class ContactController {

    private ContactService contactService;

    private ContactTypeService contactTypeService;

    @Autowired
    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }

    @Autowired
    public void setContactTypeService(ContactTypeService contactTypeService) {
        this.contactTypeService = contactTypeService;
    }

    @GetMapping("/addContact")
    public void addContactToDB(Contact contact) {
        ContactType contactType = contactTypeService.findContactType("15b28f3c-2309-11e9-ab14-d663bd873d93");
        contact.setContactType(contactType);
        contact.setFirstName("TestFN");
        contact.setLastName("TestLN");

        contactService.addContact(contact);
        System.out.println(contactService.findContact(contact.getUuid()));
    }
}
