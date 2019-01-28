package ru.geekbrains.gkportal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.gkportal.entities.Contact;
import ru.geekbrains.gkportal.repository.ContactRepository;

import java.util.Optional;

@Service
public class ContactService {

    private ContactRepository contactRepository;

    @Autowired
    public void setContactRepository(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public void addContact(Contact contact) {
        contactRepository.save(contact);
    }

    public Contact findContact(String uuid) {
        Optional<Contact> contact = contactRepository.findById(uuid);
        return contact.orElse(null);
    }

}
