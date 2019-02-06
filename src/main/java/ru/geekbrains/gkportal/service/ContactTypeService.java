package ru.geekbrains.gkportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.gkportal.entity.ContactType;
import ru.geekbrains.gkportal.repository.ContactTypeRepository;

import java.util.List;

@Service
public class ContactTypeService {
    private ContactTypeRepository contactTypeRepository;

    @Autowired
    public void setContactTypeRepository(ContactTypeRepository contactTypeRepository) {
        this.contactTypeRepository = contactTypeRepository;
    }

    public List<ContactType> getAllContactTypes() {
        List<ContactType> contactTypeList = contactTypeRepository.findAll();
        return contactTypeList;
    }
}
