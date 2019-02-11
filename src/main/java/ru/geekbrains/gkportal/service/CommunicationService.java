package ru.geekbrains.gkportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.gkportal.dto.SystemAccountDTO;
import ru.geekbrains.gkportal.entity.Communication;
import ru.geekbrains.gkportal.entity.CommunicationType;
import ru.geekbrains.gkportal.entity.Contact;
import ru.geekbrains.gkportal.repository.CommunicationRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CommunicationService {

    private static final String DEFAULT_DESCRIPTION = "Основной контакт";
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

    public List<Communication> getOrCreateCommunications(SystemAccountDTO systemAccount, Contact contact) throws Throwable {

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

    private Communication getOrCreateCommunication(SystemAccountDTO systemAccount, Contact contact, CommunicationType communicationType, String identify) {
        Communication communication;
        if ((communication = communicationRepository.findCommunicationByCommunicationTypeAndIdentifyAndContact(communicationType, identify, contact)) == null) {
            communication = createCommunication(systemAccount, contact, communicationType);


        }
        if (communicationType.getDescription().equals(CommunicationTypeService.EMAIL_DESCRIPTION) && !communication.isConfirmed()) {
//            mailService.sendRegistrationMail(contact, communication);
        }

        return communication;
    }


    public Communication createCommunication(SystemAccountDTO systemAccount, Contact contact, CommunicationType communicationType) {
        return Communication.builder()
                .communicationType(communicationType)
                .identify(communicationType.getUuid().equals(CommunicationTypeService.EMAIL_TYPE_GUID) ? systemAccount.getEmail() : systemAccount.getPhoneNumber())
                .confirmCode(UUID.randomUUID().toString())
                .confirmCodeDate(LocalDateTime.now())
                .confirmed(false)
                .description(DEFAULT_DESCRIPTION)
                .contact(contact)
                .build();
    }

}
