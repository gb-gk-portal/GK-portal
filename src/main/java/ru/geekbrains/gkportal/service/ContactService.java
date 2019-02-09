package ru.geekbrains.gkportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.gkportal.dto.FlatRegDTO;
import ru.geekbrains.gkportal.dto.OwnershipRegDTO;
import ru.geekbrains.gkportal.dto.SystemAccount;
import ru.geekbrains.gkportal.dto.SystemAccountToOwnerShip;
import ru.geekbrains.gkportal.entity.Contact;
import ru.geekbrains.gkportal.entity.Flat;
import ru.geekbrains.gkportal.entity.Ownership;
import ru.geekbrains.gkportal.repository.ContactRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContactService {

    private ContactRepository contactRepository;
    private FlatsService flatsService;
    private CommunicationService communicationService;
    private ContactTypeService contactTypeService;
    private OwnershipService ownershipService;

    @Autowired
    public void setOwnershipService(OwnershipService ownershipService) {
        this.ownershipService = ownershipService;
    }

    @Autowired
    public void setContactRepository(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Autowired
    public void setFlatsService(FlatsService flatsService) {
        this.flatsService = flatsService;
    }

    @Autowired
    public void setCommunicationService(CommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    @Autowired
    public void setContactTypeService(ContactTypeService contactTypeService) {
        this.contactTypeService = contactTypeService;
    }

    public Contact findById(String id) {
        return contactRepository.findById(id).orElse(null);
    }

    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    public void saveAll(List<Contact> contactList) {
        contactRepository.saveAll(contactList);
    }

    @Transactional
    public void save(Contact contact) {
        contactRepository.save(contact);
    }

    public Contact createContact(SystemAccount systemAccount) throws Throwable {
        List<Flat> flatArray = new ArrayList<>();
        for (FlatRegDTO flatDTO : systemAccount.getFlats()
        ) {
            flatArray.add(flatsService.createFlat(flatDTO));
        }
        Contact contact = Contact.builder()
                .contactType(systemAccount.getContactType())
                .firstName(systemAccount.getFirstName())
                .lastName(systemAccount.getLastName())
                .middleName(systemAccount.getMiddleName())
                .flats(flatArray)
                .build();

        contact.setCommunications(communicationService.createCommunication(systemAccount, contact));
        return contact;
    }

    public Contact getOrCreateContact(SystemAccountToOwnerShip systemAccount) throws Throwable {
        //todo обрезать пробелы, первую букву к верхнему регистру, остальные к нижнему
        Contact contact = contactRepository.findByFirstNameAndLastNameAndMiddleName(systemAccount.getFirstName(),
                systemAccount.getLastName(), systemAccount.getMiddleName());
        if (contact == null) {
            //todo обрезать пробелы, первую букву к верхнему регистру, остальные к нижнему
            contact = Contact.builder()
                    .contactType(systemAccount.getContactType())
                    .firstName(systemAccount.getFirstName())
                    .lastName(systemAccount.getLastName())
                    .middleName(systemAccount.getMiddleName())
                    .build();
        }

        contact.setCommunications(communicationService.createIfNotExistCommunication(systemAccount, contact));


        List<Ownership> ownershipsArray = new ArrayList<>();
        for (OwnershipRegDTO ownershipRegDTO : systemAccount.getOwnerships()) {
            ownershipsArray.add(ownershipService.createOrGetOwnership(ownershipRegDTO, contact, true));
        }

        contact.setOwnerships(ownershipsArray);


        return contact;
    }



    /*public Contact createContact(AnswerResultDTO answerResultDTO) throws Throwable {

        Contact contact = Contact.builder()
                .contactType(contactTypeService.getContactTypeByDescription("Собственник"))
                .firstName(answerResultDTO.getFullName())
                .lastName("TOSPLIT")
                .build();

        //contact.setCommunications(communicationService.createCommunication(answerResultDTO, contact));
        return contact;


    }
*/
}

