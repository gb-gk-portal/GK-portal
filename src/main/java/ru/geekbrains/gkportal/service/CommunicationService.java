package ru.geekbrains.gkportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.gkportal.entity.Communication;
import ru.geekbrains.gkportal.entity.Contact;
import ru.geekbrains.gkportal.entity.SystemAccount;
import ru.geekbrains.gkportal.repository.CommunicationRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
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

    public Contact confirmAccountAndGetContact(String mail, String code) {
        try {
            Communication communication =
                    communicationRepository.findCommunicationByCommunicationTypeAndIdentify(communicationTypeService.findEmailType(), mail);

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

    public List<Communication> createCommunication(SystemAccount systemAccount, Contact contact) throws Throwable {

        Communication phoneCommunication = Communication.builder()
                .communicationType(communicationTypeService.findPhoneType())
                .identify(systemAccount.getPhoneNumber())
                .confirmCode(UUID.randomUUID().toString())
                .confirmCodeDate(LocalDateTime.now())
                .confirmed(false)
                .description(DEFAULT_DESCRIPTION)
                .contact(contact)
                .build();

        Communication emailCommunication = Communication.builder()
                .communicationType(communicationTypeService.findEmailType())
                .identify(systemAccount.getEmail())
                .confirmCode(UUID.randomUUID().toString())
                .confirmCodeDate(LocalDateTime.now())
                .confirmed(false)
                .description(DEFAULT_DESCRIPTION)
                .contact(contact)
                .build();
        mailService.sendRegistrationMail(contact, emailCommunication);

        return Arrays.asList(phoneCommunication, emailCommunication);
    }

}
