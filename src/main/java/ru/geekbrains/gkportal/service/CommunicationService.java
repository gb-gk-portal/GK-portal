package ru.geekbrains.gkportal.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.gkportal.GkPortalApplication;
import ru.geekbrains.gkportal.dto.interfaces.SystemAccountDTO;
import ru.geekbrains.gkportal.entity.Communication;
import ru.geekbrains.gkportal.entity.CommunicationType;
import ru.geekbrains.gkportal.entity.Contact;
import ru.geekbrains.gkportal.repository.CommunicationRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class CommunicationService {

    private static final Logger logger = Logger.getLogger(GkPortalApplication.class);

    private static final String DEFAULT_DESCRIPTION = "Основной контакт";
    private static final String OTHER_DESCRIPTION = "Дополнительный контакт";
    private CommunicationRepository communicationRepository;
    private CommunicationTypeService communicationTypeService;
    private MailService mailService;

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setCommunicationRepository(CommunicationRepository communicationRepository) {
        this.communicationRepository = communicationRepository;
    }

    @Autowired
    public void setCommunicationTypeService(CommunicationTypeService communicationTypeService) {
        this.communicationTypeService = communicationTypeService;
    }

    public Contact confirmAccountAndGetContact(String mail, String code, Contact contact) {
        try {
            Communication communication =
                    communicationRepository.findCommunicationByCommunicationTypeAndIdentifyAndContact(communicationTypeService.findEmailType(), mail, contact);

            if (communication.getConfirmCode().equals(code)) {
                communication.setConfirmed(true);
                communicationRepository.save(communication);
                return communication.getContact();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }

        return null;
    }

    public List<Communication> getOrCreateCommunications(
            SystemAccountDTO systemAccount, Contact contact) throws Throwable {

        Communication phoneCommunication = getOrCreateCommunication(
                systemAccount,
                contact,
                communicationTypeService.findPhoneType(),
                systemAccount.getPhoneNumber());

        Communication emailCommunication = getOrCreateCommunication(
                systemAccount,
                contact,
                communicationTypeService.findEmailType(),
                systemAccount.getEmail());

        List<Communication> communications = new ArrayList<>();
        communications.add(phoneCommunication);
        communications.add(emailCommunication);
        return communications;
    }

    private Communication getOrCreateCommunication(SystemAccountDTO systemAccount, Contact contact,
                                                   CommunicationType communicationType, String identify) {
        Communication communication;
        if ((communication = communicationRepository.findCommunicationByCommunicationTypeAndIdentifyAndContact
                (communicationType, identify, contact)) == null) {
            communication = createCommunication(systemAccount, contact, communicationType);
        }
        if (communicationType.getDescription().equals(CommunicationTypeService.EMAIL_DESCRIPTION) && !communication.isConfirmed()) {
//            mailService.sendRegistrationMail(contact, communication);
        }

        return communication;
    }

    public Communication createCommunication(SystemAccountDTO systemAccount, Contact contact, CommunicationType communicationType) {
        Communication communication = Communication.builder()
                .communicationType(communicationType)
                .identify(communicationType.getUuid().equals(CommunicationTypeService.EMAIL_TYPE_GUID) ? systemAccount.getEmail() : systemAccount.getPhoneNumber())
                .confirmCode(UUID.randomUUID().toString())
                .confirmCodeDate(LocalDateTime.now())
                .confirmed(false)
                .description(DEFAULT_DESCRIPTION)
                .contact(contact)
                .build();
        // проверяем, что если есть другие емайл или почта, то их описание надо сменить с основного на другое
        Collection<Communication> listComm = contact.getCommunications();
        if (listComm != null)
            for (Communication com : contact.getCommunications()) {
                if (com.getCommunicationType().getUuid().equals(communicationType.getUuid()) && !com.getIdentify().equals(communication.getIdentify())) {
                    com.setDescription(OTHER_DESCRIPTION);
                }
            }
        return communication;
    }

    public String getMail(Collection<Communication> communications) {
        for (Communication communication : communications) {
            if (communication.getCommunicationType().getUuid().equals(CommunicationTypeService.EMAIL_TYPE_GUID)) {
                return communication.getIdentify();
            }

        }
        return null;
    }

    //todo может же быть несколько емейлов, нужно понять который из них главный
    public Communication getMailCommunication(Collection<Communication> communications) {
        for (Communication communication : communications) {
            if (communication.getCommunicationType().getUuid().equals(CommunicationTypeService.EMAIL_TYPE_GUID)) {
                return communication;
            }

        }
        return null;
    }

    public List<Contact> getContactListByIdentify(CommunicationType communicationType, String identidy) {
        List<Communication> communicationList =
                communicationRepository.findAllByCommunicationTypeAndIdentify(communicationType, identidy);
        List<Contact> contacts = new ArrayList<>();
        for (Communication communication : communicationList) {
            // возвращаем только основной контракт
            if (communication.getDescription().equals(DEFAULT_DESCRIPTION))
                contacts.add(communication.getContact());
        }
        return contacts;

    }

    public List<Contact> getContactListByEmail(String identidy) throws Throwable {
        return getContactListByIdentify(communicationTypeService.findEmailType(), identidy);
    }

    public List<Contact> getContactListByPhone(String identidy) throws Throwable {
        return getContactListByIdentify(communicationTypeService.findPhoneType(), identidy);
    }
}
