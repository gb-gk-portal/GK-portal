package ru.geekbrains.gkportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.gkportal.dto.OwnershipRegDTO;
import ru.geekbrains.gkportal.dto.SystemAccount;
import ru.geekbrains.gkportal.dto.SystemAccountDTO;
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
    public Contact save(Contact contact) {
        return contactRepository.save(contact);
    }

    public Contact getOrCreateContact(SystemAccount systemAccount) throws Throwable {

        Contact contact = getContact(systemAccount);
        if (contact == null) {
            contact = Contact.builder()
                    .contactType(systemAccount.getContactType())
                    .firstName(systemAccount.getFirstName())
                    .lastName(systemAccount.getLastName())
                    .middleName(systemAccount.getMiddleName())
                    .flats(flatsService.createFlats(systemAccount))
                    .build();
        }

        contact.setCommunications(communicationService.getOrCreateCommunications(systemAccount, contact));
        return contact;
    }

    public Contact getOrCreateContact(SystemAccountToOwnerShip systemAccount) throws Throwable {
        Contact contact = getContact(systemAccount);
        if (contact == null) {
            //todo обрезать пробелы, первую букву к верхнему регистру, остальные к нижнему
            contact = Contact.builder()
                    .contactType(systemAccount.getContactType())
                    .firstName(systemAccount.getFirstName())
                    .lastName(systemAccount.getLastName())
                    .middleName(systemAccount.getMiddleName())
                    .build();
        }

        contact.setCommunications(communicationService.getOrCreateCommunications(systemAccount, contact));

        //todo А если контакт уже существует и есть недвижимость ?!!
        List<Ownership> ownershipsArray = new ArrayList<>();
        for (OwnershipRegDTO ownershipRegDTO : systemAccount.getOwnerships()) {
            ownershipsArray.add(ownershipService.createOrGetOwnership(ownershipRegDTO, contact, true));
        }

        contact.setOwnerships(ownershipsArray);

        if (contact.getFlats() == null) contact.setFlats(new ArrayList<>());
        for (Ownership ownership : contact.getOwnerships()) {

            if (ownership.getOwnershipType().getUuid().equals(OwnershipTypeService.FLAT_TYPE_UUID)) {
                Flat flat;
                if (ownership.is_build_num()) {
                    flat = flatsService.getFlatByHouseAndFlatNum(ownership.getHouseBuildNum(), Integer.valueOf(ownership.getBuildNumber()), true);
                } else {
                    flat = flatsService.getFlatByHouseAndFlatNum(ownership.getHouseNum(), Integer.valueOf(ownership.getNumber()), false);
                }
                // todo вот тут хрень наверно,  потом подумать
                if (flat != null) {

                    contact.getFlats().add(flat);
                }
            }

        }


        return contact;
    }

    public Contact getContact(SystemAccountDTO systemAccount) {
        return contactRepository.findByFirstNameAndLastNameAndMiddleName(
                systemAccount.getFirstName(),
                systemAccount.getLastName(),
                systemAccount.getMiddleName()
        );
    }

//    public Contact getContact(SystemAccountToOwnerShip systemAccount) {
//        return contactRepository.findByFirstNameAndLastNameAndMiddleName(
//                systemAccount.getFirstName(),
//                systemAccount.getLastName(),
//                systemAccount.getMiddleName()
//        );
//    }
//
//    public Contact getContact(SystemAccount systemAccount) {
//        return contactRepository.findByFirstNameAndLastNameAndMiddleName(
//                systemAccount.getFirstName(),
//                systemAccount.getLastName(),
//                systemAccount.getMiddleName()
//        );
//    }


    public Contact getContactByID(String guid) {
        return contactRepository.findById(guid).get();
    }

}

