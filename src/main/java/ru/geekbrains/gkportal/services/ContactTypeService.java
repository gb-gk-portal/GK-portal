package ru.geekbrains.gkportal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.gkportal.entities.ContactType;
import ru.geekbrains.gkportal.repository.ContactTypeRepository;

import java.util.Optional;

@Service
public class ContactTypeService {

    private ContactTypeRepository contactTypeRepository;

    @Autowired
    public void setContactTypeRepository(ContactTypeRepository contactTypeRepository) {
        this.contactTypeRepository = contactTypeRepository;
    }

    public ContactType findContactType(String uuid) {
        Optional<ContactType> contactType = contactTypeRepository.findById(uuid);
        return contactType.orElse(null);
    }
}
